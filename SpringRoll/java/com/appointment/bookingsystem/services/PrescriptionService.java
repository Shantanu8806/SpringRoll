package com.appointment.bookingsystem.services;

import com.appointment.bookingsystem.entity.Prescription;
import com.appointment.bookingsystem.entity.Doctor;
import com.appointment.bookingsystem.entity.Appointment;
import com.appointment.bookingsystem.repository.PrescriptionRepository;
import com.appointment.bookingsystem.repository.DoctorRepository;
import com.appointment.bookingsystem.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    // Create a new prescription
    public Prescription createPrescription(Prescription prescription) {
        if (prescription.getAppointment() == null || prescription.getDoctor() == null) {
            throw new IllegalArgumentException("Appointment and Doctor must be provided");
        }

        Optional<Appointment> appointmentOpt = appointmentRepository.findById(prescription.getAppointment().getId());
        if (appointmentOpt.isEmpty()) {
            throw new RuntimeException("Appointment not found");
        }

        Optional<Doctor> doctorOpt = doctorRepository.findById(prescription.getDoctor().getId());
        if (doctorOpt.isEmpty()) {
            throw new RuntimeException("Doctor not found");
        }

        prescription.setAppointment(appointmentOpt.get());
        prescription.setDoctor(doctorOpt.get());

        return prescriptionRepository.save(prescription);
    }

    // Read all prescriptions
    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    // Read a prescription by ID
    public Optional<Prescription> getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id);
    }

    // Update prescription
    public Prescription updatePrescription(Long id, Prescription updatedPrescription) {
        Prescription existing = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        existing.setDiagnosis(updatedPrescription.getDiagnosis());
        existing.setMedicines(updatedPrescription.getMedicines());
        existing.setAdvice(updatedPrescription.getAdvice());

        return prescriptionRepository.save(existing);
    }

    // Delete prescription
    public void deletePrescription(Long id) {
        if (!prescriptionRepository.existsById(id)) {
            throw new RuntimeException("Prescription not found");
        }
        prescriptionRepository.deleteById(id);
    }

    // Get prescriptions by doctor
    public List<Prescription> getPrescriptionsByDoctor(Long doctorId) {
        return prescriptionRepository.findByDoctorId(doctorId);
    }

    // Get prescription by appointment
    public Prescription getPrescriptionByAppointment(Long appointmentId) {
        return prescriptionRepository.findByAppointmentId(appointmentId);
    }
}
