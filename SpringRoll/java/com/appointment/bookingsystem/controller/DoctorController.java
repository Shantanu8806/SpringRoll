package com.appointment.bookingsystem.controller;
import com.appointment.bookingsystem.repository.AppointmentRepository;
import com.appointment.bookingsystem.entity.Appointment;
import com.appointment.bookingsystem.entity.AppointmentStatus;
import com.appointment.bookingsystem.entity.Doctor;
import com.appointment.bookingsystem.services.AppointmentService;
import com.appointment.bookingsystem.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "http://localhost:4200")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AppointmentService appointmentService;

    // ✅ Create a doctor
    @PostMapping("/create")
    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
        Doctor created = doctorService.createDoctor(doctor);
        return ResponseEntity.status(201).body(created);
    }

    // ✅ Get doctor profile
    @GetMapping("/{doctorId}")
    public ResponseEntity<Doctor> getDoctorProfile(@PathVariable Long doctorId) {
        return doctorService.getDoctorProfile(doctorId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Get all doctors
    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return doctors.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(doctors);
    }

    // ✅ Update doctor
    @PutMapping("/{doctorId}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long doctorId,
            @RequestBody Doctor updatedDoctor) {
        try {
            Doctor doctor = doctorService.updateDoctor(doctorId, updatedDoctor);
            return ResponseEntity.ok(doctor);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ✅ Delete doctor
    @DeleteMapping("/{doctorId}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long doctorId) {
        try {
            doctorService.deleteDoctor(doctorId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Update availability
    @PutMapping("/{doctorId}/availability")
    public ResponseEntity<Doctor> setAvailability(@PathVariable Long doctorId,
            @RequestParam boolean available) {
        try {
            Doctor doctor = doctorService.updateAvailability(doctorId, available);
            return ResponseEntity.ok(doctor);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Doctor cancels an appointment manually if patient didn't arrive
    @PutMapping("/appointments/{appointmentId}/cancel")
    public ResponseEntity<Appointment> cancelAppointment(@PathVariable Long appointmentId) {
        Appointment cancelledAppointment = appointmentService.cancelAppointment(appointmentId);
        if (cancelledAppointment != null) {
            return ResponseEntity.ok(cancelledAppointment);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // ✅ Get all appointments for a doctor
    @GetMapping("/{doctorId}/appointments")
    public ResponseEntity<List<Appointment>> getAllAppointments(@PathVariable Long doctorId) {
        List<Appointment> appointments = doctorService.getAllAppointments(doctorId);
        return appointments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(appointments);
    }

    public List<Appointment> filterByStatus(Long doctorId, String statusStr) {
        AppointmentStatus status;
        try {
            status = AppointmentStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid appointment status: " + statusStr);
        }
        return appointmentRepository.findByDoctor_IdAndStatus(doctorId, status);
    }

    // Filter by date
    public List<Appointment> filterByDate(Long doctorId, LocalDateTime dateTime) {
        return appointmentRepository.findByDoctorIdAndAppointmentDate(doctorId, dateTime.toLocalDate());
    }

    // ✅ Filter appointments by patient name
    @GetMapping("/{doctorId}/appointments/patient")
    public ResponseEntity<List<Appointment>> filterByPatient(@PathVariable Long doctorId,
            @RequestParam String name) {
        List<Appointment> filtered = doctorService.filterByPatientName(doctorId, name);
        return filtered.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(filtered);
    }
}
