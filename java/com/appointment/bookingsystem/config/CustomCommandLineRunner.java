package com.appointment.bookingsystem.config;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import com.appointment.bookingsystem.entity.Appointment;
import com.appointment.bookingsystem.services.AppointmentService;

@Configuration
public class CustomCommandLineRunner implements CommandLineRunner {

	@Autowired
	private AppointmentService appointmentService;
	
	
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		appointmentService.createAppointment(new Appointment("Patient 1","Doctor 1",LocalDateTime.of(2025, 12, 9, 12, 0),"cold"));
		appointmentService.createAppointment(new Appointment("Patient 2","Doctor 2",LocalDateTime.of(2025, 12, 10, 13, 0),"fever"));
		appointmentService.createAppointment(new Appointment("Patient 3","Doctor 3",LocalDateTime.of(2025, 12, 11, 14, 0),"headache"));
	}

	
}
