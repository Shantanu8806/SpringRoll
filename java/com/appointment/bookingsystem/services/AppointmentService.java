package com.appointment.bookingsystem.services;

import com.appointment.bookingsystem.entity.Appointment;
import com.appointment.bookingsystem.entity.AppointmentStatus;
import com.appointment.bookingsystem.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAllByOrderByAppointmentDateTimeAsc();
    }

    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    public Appointment createAppointment(Appointment appointment) {
        validateAppointment(appointment);

        if (!isTimeSlotAvailable(appointment.getDoctorName(), appointment.getAppointmentDateTime())) {
            throw new IllegalArgumentException("Time slot is not available for doctor: " + appointment.getDoctorName());
        }

        if (appointment.getStatus() == null) {
            appointment.setStatus(AppointmentStatus.SCHEDULED);
        }

        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointment(Long id, Appointment appointmentDetails) {
        if (id == null) {
            throw new IllegalArgumentException("Appointment ID cannot be null");
        }

        if (appointmentDetails == null) {
            throw new IllegalArgumentException("Appointment details cannot be null");
        }

        Optional<Appointment> existingAppointmentOptional = appointmentRepository.findById(id);

        if (!existingAppointmentOptional.isPresent()) {
            throw new RuntimeException("Appointment not found with id: " + id);
        }

        Appointment existingAppointment = existingAppointmentOptional.get();

        validateAppointment(appointmentDetails);

        boolean doctorChanged = !existingAppointment.getDoctorName().equals(appointmentDetails.getDoctorName());
        boolean timeChanged = !existingAppointment.getAppointmentDateTime().equals(appointmentDetails.getAppointmentDateTime());

        if (doctorChanged || timeChanged) {
            boolean slotExists = appointmentRepository.existsByDoctorNameAndAppointmentDateTime(
                    appointmentDetails.getDoctorName(),
                    appointmentDetails.getAppointmentDateTime()
            );

            if (slotExists) {
                throw new IllegalArgumentException(
                        "Time slot is already booked for doctor: " + appointmentDetails.getDoctorName() +
                                " at " + appointmentDetails.getAppointmentDateTime());
            }
        }

        existingAppointment.setPatientName(appointmentDetails.getPatientName());
        existingAppointment.setDoctorName(appointmentDetails.getDoctorName());
        existingAppointment.setAppointmentDateTime(appointmentDetails.getAppointmentDateTime());
        existingAppointment.setDescription(appointmentDetails.getDescription());

        if (appointmentDetails.getStatus() != null) {
            existingAppointment.setStatus(appointmentDetails.getStatus());
        }

        return appointmentRepository.save(existingAppointment);
    }

    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new RuntimeException("Appointment not found with id: " + id);
        }
        appointmentRepository.deleteById(id);
    }

    public List<Appointment> searchByPatientName(String patientName) {
        if (patientName == null || patientName.trim().isEmpty()) {
            throw new IllegalArgumentException("Patient name cannot be empty");
        }
        return appointmentRepository.findByPatientNameContainingIgnoreCaseOrderByAppointmentDateTimeAsc(patientName);
    }

    public List<Appointment> searchByDoctorName(String doctorName) {
        if (doctorName == null || doctorName.trim().isEmpty()) {
            throw new IllegalArgumentException("Doctor name cannot be empty");
        }
        return appointmentRepository.findByDoctorNameContainingIgnoreCaseOrderByAppointmentDateTimeAsc(doctorName);
    }

    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        return appointmentRepository.findByStatus(status);
    }

    public List<Appointment> getUpcomingAppointments() {
        return appointmentRepository.findByAppointmentDateTimeAfter(LocalDateTime.now());
    }

    public List<Appointment> getAppointmentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        return appointmentRepository.findAppointmentsByDateRange(startDate, endDate);
    }

    public List<Appointment> getTodayAppointmentsByDoctor(String doctorName) {
        if (doctorName == null || doctorName.trim().isEmpty()) {
            throw new IllegalArgumentException("Doctor name cannot be empty");
        }
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);
        return appointmentRepository.findAppointmentsByDateRange(startOfDay, endOfDay).stream()
                .filter(app -> app.getDoctorName().equalsIgnoreCase(doctorName))
                .collect(Collectors.toList());
    }

    public Appointment updateAppointmentStatus(Long id, AppointmentStatus status) {
        return appointmentRepository.findById(id)
                .map(appointment -> {
                    appointment.setStatus(status);
                    return appointmentRepository.save(appointment);
                }).orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
    }

    public long getAppointmentCountByStatus(AppointmentStatus status) {
        return appointmentRepository.countByStatus(status);
    }

    public boolean isTimeSlotAvailable(String doctorName, LocalDateTime appointmentDateTime) {
        return !appointmentRepository.existsByDoctorNameAndAppointmentDateTime(doctorName, appointmentDateTime);
    }

    public List<Appointment> getAppointmentsByPatient(String patientName) {
        return appointmentRepository.findByPatientNameOrderByAppointmentDateTimeDesc(patientName);
    }

    public List<Appointment> getAppointmentsByDoctor(String doctorName) {
        return appointmentRepository.findByDoctorNameOrderByAppointmentDateTimeAsc(doctorName);
    }

    public Appointment cancelAppointment(Long id) {
        return updateAppointmentStatus(id, AppointmentStatus.CANCELLED);
    }

    public Appointment completeAppointment(Long id) {
        return updateAppointmentStatus(id, AppointmentStatus.COMPLETED);
    }

    public Appointment rescheduleAppointment(Long id, LocalDateTime newDateTime) {
        return appointmentRepository.findById(id)
                .map(appointment -> {
                    if (!isTimeSlotAvailable(appointment.getDoctorName(), newDateTime)) {
                        throw new IllegalArgumentException("New time slot is not available");
                    }
                    appointment.setAppointmentDateTime(newDateTime);
                    appointment.setStatus(AppointmentStatus.SCHEDULED);
                    return appointmentRepository.save(appointment);
                })
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
    }

    private void validateAppointment(Appointment appointment) {
        if (appointment.getPatientName() == null || appointment.getPatientName().trim().isEmpty()) {
            throw new IllegalArgumentException("Patient name is required");
        }

        if (appointment.getDoctorName() == null || appointment.getDoctorName().trim().isEmpty()) {
            throw new IllegalArgumentException("Doctor name is required");
        }

        if (appointment.getAppointmentDateTime() == null) {
            throw new IllegalArgumentException("Appointment date and time is required");
        }

        if (appointment.getAppointmentDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Appointment cannot be scheduled in the past");
        }

        int hour = appointment.getAppointmentDateTime().getHour();
        if (hour < 8 || hour >= 18) {
            throw new IllegalArgumentException("Appointments can only be scheduled between 8 AM and 6 PM");
        }

        int dayOfWeek = appointment.getAppointmentDateTime().getDayOfWeek().getValue();
        if (dayOfWeek > 5) {
            throw new IllegalArgumentException("Appointments can only be scheduled on weekdays");
        }
    }
}
