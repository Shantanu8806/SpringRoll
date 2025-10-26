package com.appointment.bookingsystem.services;

import com.appointment.bookingsystem.entity.Appointment;
import com.appointment.bookingsystem.entity.AppointmentStatus;
import com.appointment.bookingsystem.entity.Doctor;
import com.appointment.bookingsystem.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private NotificationService notificationService;

    // --- Get all appointments ---
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAllByOrderByAppointmentDateTimeAsc();
    }

    // --- Get appointment by ID ---
    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    // --- Create new appointment ---
    public Appointment createAppointment(Appointment appointment) {
        validateAppointment(appointment);

        if (appointmentRepository.existsByDoctorAndAppointmentDateTime(
                appointment.getDoctor(), appointment.getAppointmentDateTime())) {
            throw new IllegalArgumentException("Duplicate appointment exists for this doctor at the given time.");
        }

        if (appointment.getStatus() == null) {
            appointment.setStatus(AppointmentStatus.SCHEDULED);
        }

        Appointment savedAppointment = appointmentRepository.save(appointment);

        // Create notifications for Doctor and Staff
        String message = "New appointment scheduled with Patient " +
                appointment.getPatient().getName() +
                " on " + appointment.getAppointmentDateTime().toLocalDate() +
                " at " + appointment.getAppointmentDateTime().toLocalTime();

        // Doctor notification
        notificationService.createNotification(
                new com.appointment.bookingsystem.entity.Notification(
                        null,
                        com.appointment.bookingsystem.entity.Notification.TargetUser.DOCTOR,
                        message,
                        com.appointment.bookingsystem.entity.Notification.NotificationType.INFO,
                        null,
                        false
                )
        );

        // Staff notification (if assigned)
        if (appointment.getStaff() != null) {
            notificationService.createNotification(
                    new com.appointment.bookingsystem.entity.Notification(
                            null,
                            com.appointment.bookingsystem.entity.Notification.TargetUser.STAFF,
                            message,
                            com.appointment.bookingsystem.entity.Notification.NotificationType.INFO,
                            null,
                            false
                    )
            );
        }

        return savedAppointment;
    }

    // --- Update existing appointment ---
    public Appointment updateAppointment(Long id, Appointment appointmentDetails) {
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));

        validateAppointment(appointmentDetails);

        boolean doctorChanged = !existing.getDoctor().getId().equals(appointmentDetails.getDoctor().getId());
        boolean timeChanged = !existing.getAppointmentDateTime().equals(appointmentDetails.getAppointmentDateTime());

        if (doctorChanged || timeChanged) {
            boolean slotExists = appointmentRepository.existsByDoctorAndAppointmentDateTime(
                    appointmentDetails.getDoctor(),
                    appointmentDetails.getAppointmentDateTime()
            );
            if (slotExists) {
                throw new IllegalArgumentException(
                        "Time slot is already booked for this doctor at the given time.");
            }
        }

        existing.setDoctor(appointmentDetails.getDoctor());
        existing.setPatient(appointmentDetails.getPatient());
        existing.setAppointmentDateTime(appointmentDetails.getAppointmentDateTime());
        existing.setTimeSlot(appointmentDetails.getTimeSlot());
        existing.setReasonForVisit(appointmentDetails.getReasonForVisit());
        existing.setRemarks(appointmentDetails.getRemarks());
        existing.setPaymentStatus(appointmentDetails.getPaymentStatus());

        if (appointmentDetails.getStatus() != null) {
            existing.setStatus(appointmentDetails.getStatus());
        }

        return appointmentRepository.save(existing);
    }

    // --- Update appointment status ---
    public Appointment updateAppointmentStatus(Long id, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }

    // --- Cancel appointment ---
    public Appointment cancelAppointment(Long id) {
        return updateAppointmentStatus(id, AppointmentStatus.CANCELLED);
    }

    // --- Complete appointment ---
    public Appointment completeAppointment(Long id) {
        return updateAppointmentStatus(id, AppointmentStatus.COMPLETED);
    }

    // --- Reschedule appointment ---
    public Appointment rescheduleAppointment(Long id, LocalDateTime newDateTime) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));

        if (appointmentRepository.existsByDoctorAndAppointmentDateTime(
                appointment.getDoctor(), newDateTime)) {
            throw new IllegalArgumentException("New time slot is not available for this doctor.");
        }

        appointment.setAppointmentDateTime(newDateTime);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        return appointmentRepository.save(appointment);
    }

    // --- Staff requests appointment deletion ---
    public void requestDeleteAppointment(Long appointmentId, String requestedBy) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));

        String message = "Staff " + requestedBy + " requested deletion of Appointment ID: "
                + appointmentId + " for Patient " + appointment.getPatient().getName()
                + " on " + appointment.getAppointmentDateTime().toLocalDate()
                + " at " + appointment.getAppointmentDateTime().toLocalTime();

        notificationService.createAdminDeleteRequestNotification(message);
    }

    // --- Actual deletion by admin ---
    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new RuntimeException("Appointment not found with id: " + id);
        }
        appointmentRepository.deleteById(id);
    }

    // --- Search methods ---
    public List<Appointment> searchByPatientName(String patientName) {
        return appointmentRepository.findByPatient_NameContainingIgnoreCaseOrderByAppointmentDateTimeAsc(patientName);
    }

    public List<Appointment> searchByDoctorName(String doctorName) {
        return appointmentRepository.findByDoctor_NameContainingIgnoreCaseOrderByAppointmentDateTimeAsc(doctorName);
    }

    // --- Other helpers ---
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

    public List<Appointment> getTodayAppointmentsByDoctor(Doctor doctor) {
        LocalDate today = LocalDate.now();
        return appointmentRepository.findByDoctorIdAndAppointmentDate(doctor.getId(), today);
    }

    public long getAppointmentCountByStatus(AppointmentStatus status) {
        return appointmentRepository.countByStatus(status);
    }

    public boolean isTimeSlotAvailable(Doctor doctor, LocalDateTime appointmentDateTime) {
        return !appointmentRepository.existsByDoctorAndAppointmentDateTime(doctor, appointmentDateTime);
    }

    // --- Validation ---
    private void validateAppointment(Appointment appointment) {
        if (appointment.getPatient() == null) throw new IllegalArgumentException("Patient is required");
        if (appointment.getDoctor() == null) throw new IllegalArgumentException("Doctor is required");
        if (appointment.getAppointmentDateTime() == null) throw new IllegalArgumentException("Appointment date and time is required");
        if (appointment.getAppointmentDateTime().isBefore(LocalDateTime.now())) throw new IllegalArgumentException("Appointment cannot be scheduled in the past");

        int hour = appointment.getAppointmentDateTime().getHour();
        if (hour < 8 || hour >= 18) throw new IllegalArgumentException("Appointments can only be scheduled between 8 AM and 6 PM");

        int dayOfWeek = appointment.getAppointmentDateTime().getDayOfWeek().getValue();
        if (dayOfWeek > 5) throw new IllegalArgumentException("Appointments can only be scheduled on weekdays");
    }
}
