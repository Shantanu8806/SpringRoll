package com.appointment.bookingsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotNull
    @Min(0)
    @Max(120)
    private Integer age;

    @NotBlank
    private String gender;

    @Pattern(regexp = "^[0-9]{10}$", message = "Contact must be 10 digits")
    @NotBlank
    @Column(unique = true)
    private String contact;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(max = 200)
    private String address;

    @Size(max = 500)
    private String medicalHistory;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    public Patient() {}

	public Patient(Long patientId, @NotBlank @Size(max = 100) String name, @NotNull @Min(0) @Max(120) Integer age,
			@NotBlank String gender,
			@Pattern(regexp = "^[0-9]{10}$", message = "Contact must be 10 digits") @NotBlank String contact,
			@Email @NotBlank String email, @NotBlank @Size(max = 200) String address,
			@Size(max = 500) String medicalHistory, List<Appointment> appointments) {
		super();
		this.patientId = patientId;
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.contact = contact;
		this.email = email;
		this.address = address;
		this.medicalHistory = medicalHistory;
		this.appointments = appointments;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMedicalHistory() {
		return medicalHistory;
	}

	public void setMedicalHistory(String medicalHistory) {
		this.medicalHistory = medicalHistory;
	}

	public List<Appointment> getAppointments() {
		return appointments;
	}

	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}
    
    
}

