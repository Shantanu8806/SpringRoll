package com.appointment.bookingsystem.services;

import com.appointment.bookingsystem.entity.Appointment;
import com.appointment.bookingsystem.entity.Doctor;
import com.appointment.bookingsystem.repository.AppointmentRepository;
import com.appointment.bookingsystem.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    // Get doctor profile
    public Optional<Doctor> getDoctorProfile(Long doctorId) {
        return doctorRepository.findById(doctorId);
    }

    // Get all appointments for doctor
    public List<Appointment> getAllAppointments(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    // Filter by appointment status (Booked / Completed / Cancelled)
    public List<Appointment> filterByStatus(Long doctorId, String status) {
        return appointmentRepository.findByDoctorIdAndStatus(doctorId, status);
    }

    // Filter by appointment date
    public List<Appointment> filterByDate(Long doctorId, LocalDateTime date) {
        return appointmentRepository.findByDoctorIdAndAppointmentDate(doctorId, date);
    }

    // Filter by patient name
    public List<Appointment> filterByPatientName(Long doctorId, String patientName) {
        return appointmentRepository.findByDoctorIdAndPatientName(doctorId, patientName);
    }
    
    public Doctor updateAvailability(Long doctorId, String availability) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();
            doctor.setAvailability(availability); // Available or Not Available
            return doctorRepository.save(doctor);
        }
        return null;
    }
}

