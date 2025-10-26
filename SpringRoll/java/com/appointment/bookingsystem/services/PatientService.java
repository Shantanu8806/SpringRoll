package com.appointment.bookingsystem.services;

import com.appointment.bookingsystem.entity.Patient;
import com.appointment.bookingsystem.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    // ✅ Create
    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    // ✅ Read all
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    // ✅ Read by ID
    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    // ✅ Update
    public Patient updatePatient(Long id, Patient updatedPatient) {
        return patientRepository.findById(id).map(patient -> {
            patient.setName(updatedPatient.getName());
            patient.setAge(updatedPatient.getAge());
            patient.setGender(updatedPatient.getGender());
            patient.setContact(updatedPatient.getContact());
            patient.setEmail(updatedPatient.getEmail());
            patient.setAddress(updatedPatient.getAddress());
            patient.setMedicalHistory(updatedPatient.getMedicalHistory());
            return patientRepository.save(patient);
        }).orElse(null);
    }

    // ✅ Delete
    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}
