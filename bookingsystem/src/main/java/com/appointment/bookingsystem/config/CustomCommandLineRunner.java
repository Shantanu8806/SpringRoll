package com.appointment.bookingsystem.config;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import com.appointment.bookingsystem.entity.Appointment;
import com.appointment.bookingsystem.entity.Doctor;
import com.appointment.bookingsystem.repository.DoctorRepository;
import com.appointment.bookingsystem.services.AppointmentService;

@Configuration
public class CustomCommandLineRunner implements CommandLineRunner {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public void run(String... args) throws Exception {

        // ✅ Create sample doctors
        Doctor doctor1 = new Doctor();
        doctor1.setName("Doctor 1");
        doctor1.setEmail("doctor1@example.com");
        doctor1.setSpecialization("Cardiology");
        doctorRepository.save(doctor1);

        Doctor doctor2 = new Doctor();
        doctor2.setName("Doctor 2");
        doctor2.setEmail("doctor2@example.com");
        doctor2.setSpecialization("Dermatology");
        doctorRepository.save(doctor2);

        Doctor doctor3 = new Doctor();
        doctor3.setName("Doctor 3");
        doctor3.setEmail("doctor3@example.com");
        doctor3.setSpecialization("Neurology");
        doctorRepository.save(doctor3);

        // ✅ Create sample appointments linked to doctors
        appointmentService.createAppointment(new Appointment("Patient 1", "Doctor 1", LocalDateTime.of(2025, 12, 9, 12, 0), "Cold"));
        appointmentService.createAppointment(new Appointment("Patient 2", "Doctor 2", LocalDateTime.of(2025, 12, 10, 13, 0), "Fever"));
        appointmentService.createAppointment(new Appointment("Patient 3", "Doctor 1", LocalDateTime.of(2025, 12, 11, 14, 0), "Headache"));
        appointmentService.createAppointment(new Appointment("Patient 4", "Doctor 3", LocalDateTime.of(2025, 12, 12, 15, 0), "Back Pain"));
        appointmentService.createAppointment(new Appointment("Patient 5", "Doctor 2", LocalDateTime.of(2025, 12, 13, 16, 0), "Flu"));

        System.out.println("✅ Sample doctors and appointments inserted successfully!");
    }
}
