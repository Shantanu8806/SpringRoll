package com.appointment.bookingsystem.repository;


import com.appointment.bookingsystem.entity.Appointment;
import com.appointment.bookingsystem.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientNameContainingIgnoreCase(String patientName);

    List<Appointment> findByDoctorNameContainingIgnoreCase(String doctorName);

    List<Appointment> findByStatus(AppointmentStatus status);

    List<Appointment> findByPatientNameContainingIgnoreCaseAndStatus(String patientName, AppointmentStatus status);

    List<Appointment> findByDoctorNameContainingIgnoreCaseAndStatus(String doctorName, AppointmentStatus status);

    List<Appointment> findByAppointmentDateTimeAfter(LocalDateTime dateTime);

    List<Appointment> findByAppointmentDateTimeBefore(LocalDateTime dateTime);

    @Query("SELECT a FROM Appointment a WHERE a.appointmentDateTime BETWEEN :startDate AND :endDate")
    List<Appointment> findAppointmentsByDateRange(@Param("startDate") LocalDateTime startDate,
                                                 @Param("endDate") LocalDateTime endDate);

    @Modifying
    @Transactional
    @Query("DELETE FROM Appointment a WHERE a.appointmentDateTime < :date")
    void deleteAppointmentsOlderThan(@Param("date") LocalDateTime date);

    long countByStatus(AppointmentStatus status);

    List<Appointment> findAllByOrderByAppointmentDateTimeAsc();

    List<Appointment> findByPatientNameContainingIgnoreCaseOrderByAppointmentDateTimeAsc(String patientName);

    List<Appointment> findByDoctorNameContainingIgnoreCaseOrderByAppointmentDateTimeAsc(String doctorName);

    boolean existsByDoctorNameAndAppointmentDateTime(String doctorName, LocalDateTime appointmentDateTime);

    List<Appointment> findByPatientNameOrderByAppointmentDateTimeDesc(String patientName);

    List<Appointment> findByDoctorNameOrderByAppointmentDateTimeAsc(String doctorName);

    List<Appointment> findByDoctorNameAndAppointmentDateTime(String doctorName, LocalDateTime appointmentDateTime);
}
