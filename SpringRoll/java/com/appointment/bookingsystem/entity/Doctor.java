package com.appointment.bookingsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String name;

    @NotNull
    @Column(nullable = false)
    private LocalDate dob;

    @NotNull
    @Min(0)
    private Integer experience;

    @NotBlank
    @Size(max = 100)
    private String qualification;

    @NotNull
    @Min(25)
    private Integer age;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @Pattern(regexp = "^[0-9]{10}$", message = "Contact must be 10 digits")
    @NotBlank
    @Column(nullable = false, unique = true)
    private String contact;

    @NotBlank
    private String gender;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private Double consultationFee;

    @NotBlank
    @Size(max = 100)
    private String specialization;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    @Column(nullable = false)
    private boolean availability = true; // true = Available, false = Not Available

    public Doctor() {}

    @PrePersist
    public void hashAndGenerateDefaultPassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (this.password == null && this.name != null && this.dob != null) {
            String prefix = name.replaceAll("\\s+", "").toUpperCase();
            prefix = prefix.length() >= 4 ? prefix.substring(0, 4) : prefix;
            String monthDay = String.format("%02d%02d", dob.getMonthValue(), dob.getDayOfMonth());
            this.password = prefix + monthDay + "@doc";
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

    public Doctor(Long id, String name, LocalDate dob, Integer experience, String qualification, Integer age,
                  String email, String contact, String gender, Double consultationFee, String specialization,
                  Department department, String password, List<Appointment> appointments, boolean availability) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.experience = experience;
        this.qualification = qualification;
        this.age = age;
        this.email = email;
        this.contact = contact;
        this.gender = gender;
        this.consultationFee = consultationFee;
        this.specialization = specialization;
        this.department = department;
        this.password = password;
        this.appointments = appointments;
        this.availability = availability;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }

    public Integer getExperience() { return experience; }
    public void setExperience(Integer experience) { this.experience = experience; }

    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public Double getConsultationFee() { return consultationFee; }
    public void setConsultationFee(Double consultationFee) { this.consultationFee = consultationFee; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<Appointment> getAppointments() { return appointments; }
    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }

    public boolean isAvailability() { return availability; }
    public void setAvailability(boolean availability) { this.availability = availability; }
} 

