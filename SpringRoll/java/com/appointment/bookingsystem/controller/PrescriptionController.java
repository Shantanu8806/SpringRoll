package com.appointment.bookingsystem.controller;

import com.appointment.bookingsystem.entity.Prescription;
import com.appointment.bookingsystem.services.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@CrossOrigin(origins = "http://localhost:4200")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    // 1️⃣ Create prescription
    @PostMapping
    public ResponseEntity<Prescription> createPrescription(@RequestBody Prescription prescription) {
        try {
            Prescription created = prescriptionService.createPrescription(prescription);
            return ResponseEntity.status(201).body(created);
        } catch (RuntimeException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // 2️⃣ Get all prescriptions
    @GetMapping
    public ResponseEntity<List<Prescription>> getAllPrescriptions() {
        List<Prescription> prescriptions = prescriptionService.getAllPrescriptions();
        return prescriptions.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(prescriptions);
    }

    // 3️⃣ Get prescription by ID
    @GetMapping("/{id}")
    public ResponseEntity<Prescription> getPrescriptionById(@PathVariable Long id) {
        return prescriptionService.getPrescriptionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 4️⃣ Update prescription
    @PutMapping("/{id}")
    public ResponseEntity<Prescription> updatePrescription(@PathVariable Long id, @RequestBody Prescription prescription) {
        try {
            Prescription updated = prescriptionService.updatePrescription(id, prescription);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // 5️⃣ Delete prescription
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrescription(@PathVariable Long id) {
        try {
            prescriptionService.deletePrescription(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 6️⃣ Get prescriptions by doctor
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Prescription>> getByDoctor(@PathVariable Long doctorId) {
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByDoctor(doctorId);
        return prescriptions.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(prescriptions);
    }

    // 7️⃣ Get prescription by appointment
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<Prescription> getByAppointment(@PathVariable Long appointmentId) {
        Prescription prescription = prescriptionService.getPrescriptionByAppointment(appointmentId);
        if (prescription != null) {
            return ResponseEntity.ok(prescription);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
