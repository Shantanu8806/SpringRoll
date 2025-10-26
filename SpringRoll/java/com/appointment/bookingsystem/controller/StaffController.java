package com.appointment.bookingsystem.controller;

import com.appointment.bookingsystem.entity.Staff;
import com.appointment.bookingsystem.services.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    @Autowired
    private StaffService staffService;

    // ✅ Create Staff
    @PostMapping("/create")
    public ResponseEntity<Staff> createStaff(@RequestBody Staff staff) {
        Staff created = staffService.createStaff(staff);
        return ResponseEntity.status(201).body(created);
    }

    // ✅ Get all staff
    @GetMapping
    public ResponseEntity<List<Staff>> getAllStaff() {
        List<Staff> staffList = staffService.getAllStaff();
        return staffList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(staffList);
    }

    // ✅ Get staff by ID
    @GetMapping("/{id}")
    public ResponseEntity<Staff> getStaffById(@PathVariable Long id) {
        return staffService.getStaffById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Update staff
    @PutMapping("/{id}")
    public ResponseEntity<Staff> updateStaff(@PathVariable Long id, @RequestBody Staff staff) {
        try {
            Staff updated = staffService.updateStaff(id, staff);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // ✅ Delete staff
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
        try {
            staffService.deleteStaff(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Change password
    @PutMapping("/{id}/change-password")
    public ResponseEntity<Staff> changePassword(@PathVariable Long id, @RequestParam String newPassword) {
        try {
            Staff updated = staffService.changePassword(id, newPassword);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
