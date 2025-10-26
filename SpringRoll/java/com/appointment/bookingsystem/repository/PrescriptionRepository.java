package com.appointment.bookingsystem.repository;

import com.appointment.bookingsystem.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    // Find all prescriptions by doctor
    List<Prescription> findByDoctorId(Long doctorId);

    // Find all prescriptions by appointment
    Prescription findByAppointmentId(Long appointmentId);
}
