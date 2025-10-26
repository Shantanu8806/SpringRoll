package com.appointment.bookingsystem.config;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import com.appointment.bookingsystem.entity.Appointment;
import com.appointment.bookingsystem.entity.AppointmentStatus;
import com.appointment.bookingsystem.entity.Doctor;
import com.appointment.bookingsystem.entity.Patient;
import com.appointment.bookingsystem.services.AppointmentService;

@Configuration
public class CustomCommandLineRunner implements CommandLineRunner {

    @Autowired
    private AppointmentService appointmentService;

    @Override
    public void run(String... args) throws Exception {
        // ✅ Create mock Patient and Doctor entities
        Patient p1 = new Patient();
        p1.setName("Patient 1");

        Doctor d1 = new Doctor();
        d1.setName("Doctor 1");

        Patient p2 = new Patient();
        p2.setName("Patient 2");

        Doctor d2 = new Doctor();
        d2.setName("Doctor 2");

        Patient p3 = new Patient();
        p3.setName("Patient 3");

        Doctor d3 = new Doctor();
        d3.setName("Doctor 3");

        // ✅ Create sample appointments
        Appointment a1 = new Appointment();
        a1.setPatient(p1);
        a1.setDoctor(d1);
        a1.setAppointmentDateTime(LocalDateTime.of(2025, 12, 9, 12, 0));
        a1.setReasonForVisit("Cold");
        a1.setStatus(AppointmentStatus.SCHEDULED);

        Appointment a2 = new Appointment();
        a2.setPatient(p2);
        a2.setDoctor(d2);
        a2.setAppointmentDateTime(LocalDateTime.of(2025, 12, 10, 13, 0));
        a2.setReasonForVisit("Fever");
        a2.setStatus(AppointmentStatus.SCHEDULED);

        Appointment a3 = new Appointment();
        a3.setPatient(p3);
        a3.setDoctor(d3);
        a3.setAppointmentDateTime(LocalDateTime.of(2025, 12, 11, 14, 0));
        a3.setReasonForVisit("Headache");
        a3.setStatus(AppointmentStatus.SCHEDULED);

        // ✅ Save them
        appointmentService.createAppointment(a1);
        appointmentService.createAppointment(a2);
        appointmentService.createAppointment(a3);
    }
}
