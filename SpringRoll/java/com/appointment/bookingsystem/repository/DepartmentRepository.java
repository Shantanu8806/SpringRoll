package com.appointment.bookingsystem.repository;

import com.appointment.bookingsystem.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    // Find department by name
    Department findByName(String name);

    // Check if department exists by name
    boolean existsByName(String name);
}
