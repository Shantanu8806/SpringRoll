package com.appointment.bookingsystem.services;

import com.appointment.bookingsystem.entity.Appointment;
import com.appointment.bookingsystem.entity.AppointmentStatus;
import com.appointment.bookingsystem.entity.Doctor;
import com.appointment.bookingsystem.repository.AppointmentRepository;
import com.appointment.bookingsystem.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    // ✅ Create doctor
    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    // ✅ Get doctor by ID
    public Optional<Doctor> getDoctorProfile(Long doctorId) {
        return doctorRepository.findById(doctorId);
    }

    // ✅ Get all doctors
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    // ✅ Update doctor profile
    public Doctor updateDoctor(Long doctorId, Doctor updatedDoctor) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + doctorId));

        doctor.setName(updatedDoctor.getName());
        doctor.setDob(updatedDoctor.getDob());
        doctor.setExperience(updatedDoctor.getExperience());
        doctor.setQualification(updatedDoctor.getQualification());
        doctor.setAge(updatedDoctor.getAge());
        doctor.setEmail(updatedDoctor.getEmail());
        doctor.setContact(updatedDoctor.getContact());
        doctor.setGender(updatedDoctor.getGender());
        doctor.setConsultationFee(updatedDoctor.getConsultationFee());
        doctor.setSpecialization(updatedDoctor.getSpecialization());
        doctor.setDepartment(updatedDoctor.getDepartment());

        // Only update password if provided
        if (updatedDoctor.getPassword() != null && !updatedDoctor.getPassword().isBlank()) {
            doctor.setPassword(updatedDoctor.getPassword());
        }

        return doctorRepository.save(doctor);
    }

    // ✅ Delete doctor
    public void deleteDoctor(Long doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new RuntimeException("Doctor not found with ID: " + doctorId);
        }
        doctorRepository.deleteById(doctorId);
    }

    // ✅ Update availability
    public Doctor updateAvailability(Long doctorId, boolean available) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + doctorId));
        doctor.setAvailability(available);
        return doctorRepository.save(doctor);
    }

    // ✅ Get all appointments for doctor
    public List<Appointment> getAllAppointments(Long doctorId) {
        return appointmentRepository.findByDoctor_Id(doctorId);
    }

    // ✅ Filter appointments by status
    public List<Appointment> filterByStatus(Long doctorId, AppointmentStatus status) {
        return appointmentRepository.findByDoctor_IdAndStatus(doctorId, status);
    }

    // ✅ Filter appointments by exact date
    public List<Appointment> filterByDate(Long doctorId, LocalDate date) {
        return appointmentRepository.findByDoctorIdAndAppointmentDate(doctorId, date);
    }

    // ✅ Filter appointments by patient name
    public List<Appointment> filterByPatientName(Long doctorId, String patientName) {
        return appointmentRepository.findByDoctorIdAndPatientName(doctorId, patientName);
    }
}
