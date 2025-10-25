package com.appointment.bookingsystem.controller;

import com.appointment.bookingsystem.entity.Appointment;
import com.appointment.bookingsystem.entity.Doctor;
import com.appointment.bookingsystem.services.AppointmentService;
import com.appointment.bookingsystem.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/doctors/dashboard")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    // 1️⃣ Doctor profile details
    @GetMapping("/{doctorId}/profile")
    public ResponseEntity<Doctor> getDoctorProfile(@PathVariable Long doctorId) {
        return doctorService.getDoctorProfile(doctorId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 2️⃣ All appointments
    @GetMapping("/{doctorId}/appointments")
    public ResponseEntity<List<Appointment>> getAllAppointments(@PathVariable Long doctorId) {
        List<Appointment> appointments = doctorService.getAllAppointments(doctorId);
        return appointments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(appointments);
    }

    // 3️⃣ Filter by status
    @GetMapping("/{doctorId}/appointments/status")
    public ResponseEntity<List<Appointment>> filterByStatus(@PathVariable Long doctorId,
                                                            @RequestParam String status) {
        List<Appointment> filtered = doctorService.filterByStatus(doctorId, status);
        return filtered.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(filtered);
    }

    // 4️⃣ Filter by date
    @GetMapping("/{doctorId}/appointments/date")
    public ResponseEntity<List<Appointment>> filterByDate(@PathVariable Long doctorId,
                                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime date) {
        List<Appointment> filtered = doctorService.filterByDate(doctorId, date);
        return filtered.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(filtered);
    }

    // 5️⃣ Filter by patient name
    @GetMapping("/{doctorId}/appointments/patient")
    public ResponseEntity<List<Appointment>> filterByPatient(@PathVariable Long doctorId,
                                                             @RequestParam String name) {
        List<Appointment> filtered = doctorService.filterByPatientName(doctorId, name);
        return filtered.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(filtered);
    }
    
    @PutMapping("/{doctorId}/availability")
    public ResponseEntity<Doctor> setAvailability(@PathVariable Long doctorId,
                                                  @RequestParam String availability) {
        Doctor updatedDoctor = doctorService.updateAvailability(doctorId, availability);
        if (updatedDoctor != null) {
            return ResponseEntity.ok(updatedDoctor);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @Autowired
    private AppointmentService appointmentService;

    // ✅ Doctor cancels an appointment manually if patient didn't arrive
    @PutMapping("/{appointmentId}/cancel")
    public ResponseEntity<Appointment> cancelAppointment(@PathVariable Long appointmentId) {
        Appointment cancelledAppointment = appointmentService.cancelAppointment(appointmentId);
        if (cancelledAppointment != null) {
            return ResponseEntity.ok(cancelledAppointment);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
