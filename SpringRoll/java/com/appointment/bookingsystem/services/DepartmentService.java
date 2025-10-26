package com.appointment.bookingsystem.services;

import com.appointment.bookingsystem.entity.Department;
import com.appointment.bookingsystem.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    // Get all departments
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    // Get department by ID
    public Optional<Department> getDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }

    // Create a new department
    public Department createDepartment(Department department) {
        if (departmentRepository.existsByName(department.getName())) {
            throw new IllegalArgumentException("Department with name '" + department.getName() + "' already exists");
        }
        return departmentRepository.save(department);
    }

    // Update an existing department
    public Department updateDepartment(Long id, Department departmentDetails) {
        return departmentRepository.findById(id).map(department -> {
            department.setName(departmentDetails.getName());
            department.setDescription(departmentDetails.getDescription());
            return departmentRepository.save(department);
        }).orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
    }

    // Delete a department
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new RuntimeException("Department not found with id: " + id);
        }
        departmentRepository.deleteById(id);
    }
}
