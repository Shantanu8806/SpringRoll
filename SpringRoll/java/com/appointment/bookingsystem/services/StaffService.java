package com.appointment.bookingsystem.services;

import com.appointment.bookingsystem.entity.Staff;
import com.appointment.bookingsystem.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffService {

    @Autowired
    private StaffRepository staffRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // ✅ Create Staff
    public Staff createStaff(Staff staff) {
        if (staff.getPassword() == null || staff.getPassword().trim().isEmpty()) {
            // default password already generated in entity @PrePersist
        } else {
            staff.setPassword(encoder.encode(staff.getPassword()));
        }
        return staffRepository.save(staff);
    }

    // ✅ Read all staff
    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    // ✅ Read single staff by ID
    public Optional<Staff> getStaffById(Long id) {
        return staffRepository.findById(id);
    }

    // ✅ Update staff
    public Staff updateStaff(Long id, Staff updatedStaff) {
        Staff existing = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found with id: " + id));

        existing.setName(updatedStaff.getName());
        existing.setDob(updatedStaff.getDob());
        existing.setEmail(updatedStaff.getEmail());
        existing.setContact(updatedStaff.getContact());
        existing.setRole(updatedStaff.getRole());
        existing.setAssignedDepartment(updatedStaff.getAssignedDepartment());

        return staffRepository.save(existing);
    }

    // ✅ Delete staff
    public void deleteStaff(Long id) {
        if (!staffRepository.existsById(id)) {
            throw new RuntimeException("Staff not found with id: " + id);
        }
        staffRepository.deleteById(id);
    }

    // ✅ Change password
    public Staff changePassword(Long id, String newPassword) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found with id: " + id));

        staff.setPassword(encoder.encode(newPassword));
        return staffRepository.save(staff);
    }

    // ✅ Find by email (for login)
    public Optional<Staff> findByEmail(String email) {
        return staffRepository.findByEmail(email);
    }

}
