package com.appointment.bookingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.appointment.bookingsystem.entity.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    
    // Custom finders (optional)
    Patient findByEmail(String email);
    Patient findByContact(String contact);
}
