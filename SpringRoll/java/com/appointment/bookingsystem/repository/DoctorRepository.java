package com.appointment.bookingsystem.repository;

import com.appointment.bookingsystem.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    // Find by email (for login/authentication)
    Optional<Doctor> findByEmail(String email);

    // Search doctors by name
    List<Doctor> findByNameContainingIgnoreCase(String name);

    // Search doctors by specialization
    List<Doctor> findBySpecializationContainingIgnoreCase(String specialization);

    // Find doctors by department
    List<Doctor> findByDepartmentId(Long departmentId);

    // Get only available doctors
    List<Doctor> findByAvailabilityTrue();
}
