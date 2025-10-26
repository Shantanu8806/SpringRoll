package com.appointment.bookingsystem.repository;

import com.appointment.bookingsystem.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    
    Optional<Staff> findByEmail(String email);
    
    Optional<Staff> findByContact(String contact);
    
    boolean existsByEmail(String email);
    
    boolean existsByContact(String contact);
}
