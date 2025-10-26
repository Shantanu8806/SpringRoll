package com.appointment.bookingsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "staff")
public class Staff {

    public enum Role {
        STAFF, MANAGER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long staffId;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotNull
    @Column(nullable = false)
    private LocalDate dob;

    @Pattern(regexp = "^[0-9]{10}$")
    @NotBlank
    @Column(unique = true)
    private String contact;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "assigned_department", nullable = false)
    private Department assignedDepartment;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    @Column(nullable = false)
    private boolean available = true;

    public Staff() {}

    @PrePersist
    public void hashAndGenerateDefaultPassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (this.password == null && this.name != null && this.dob != null) {
            String prefix = name.replaceAll("\\s+", "").toUpperCase();
            prefix = prefix.length() >= 4 ? prefix.substring(0, 4) : prefix;
            String monthDay = String.format("%02d%02d", dob.getMonthValue(), dob.getDayOfMonth());
            this.password = prefix + monthDay + "@staff";
        }
        if (!this.password.startsWith("$2a$")) {
            this.password = encoder.encode(this.password);
        }
    }

    @PreUpdate
    public void hashUpdatedPassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (this.password != null && !this.password.startsWith("$2a$")) {
            this.password = encoder.encode(this.password);
        }
    }

    public Staff(Long staffId, String name, LocalDate dob, String contact, String email,
                 Role role, Department assignedDepartment, String password,
                 List<Appointment> appointments, boolean available) {
        this.staffId = staffId;
        this.name = name;
        this.dob = dob;
        this.contact = contact;
        this.email = email;
        this.role = role;
        this.assignedDepartment = assignedDepartment;
        this.password = password;
        this.appointments = appointments;
        this.available = available;
    }

    // Getters and Setters
    public Long getStaffId() { return staffId; }
    public void setStaffId(Long staffId) { this.staffId = staffId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public Department getAssignedDepartment() { return assignedDepartment; }
    public void setAssignedDepartment(Department assignedDepartment) { this.assignedDepartment = assignedDepartment; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public List<Appointment> getAppointments() { return appointments; }
    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}
