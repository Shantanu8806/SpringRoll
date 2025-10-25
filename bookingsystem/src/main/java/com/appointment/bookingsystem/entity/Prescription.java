package com.appointment.bookingsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "prescriptions")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prescriptionId;

    @OneToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @NotBlank
    @Size(max = 250)
    private String diagnosis;

    @NotBlank
    @Size(max = 500)
    private String medicines;

    @Size(max = 300)
    private String advice;

    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    public Prescription() {}

	public Prescription(Long prescriptionId, Appointment appointment, Doctor doctor,
			@NotBlank @Size(max = 250) String diagnosis, @NotBlank @Size(max = 500) String medicines,
			@Size(max = 300) String advice, @NotNull LocalDateTime createdAt) {
		super();
		this.prescriptionId = prescriptionId;
		this.appointment = appointment;
		this.doctor = doctor;
		this.diagnosis = diagnosis;
		this.medicines = medicines;
		this.advice = advice;
		this.createdAt = createdAt;
	}

	public Long getPrescriptionId() {
		return prescriptionId;
	}

	public void setPrescriptionId(Long prescriptionId) {
		this.prescriptionId = prescriptionId;
	}

	public Appointment getAppointment() {
		return appointment;
	}

	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getMedicines() {
		return medicines;
	}

	public void setMedicines(String medicines) {
		this.medicines = medicines;
	}

	public String getAdvice() {
		return advice;
	}

	public void setAdvice(String advice) {
		this.advice = advice;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
    
    
}

