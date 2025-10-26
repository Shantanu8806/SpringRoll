package com.appointment.bookingsystem.repository;

import com.appointment.bookingsystem.entity.Appointment;
import com.appointment.bookingsystem.entity.AppointmentStatus;
import com.appointment.bookingsystem.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Search by patient or doctor name (partial, case-insensitive)
    List<Appointment> findByPatient_NameContainingIgnoreCase(String patientName);
    List<Appointment> findByDoctor_NameContainingIgnoreCase(String doctorName);

    // Filter by status
    List<Appointment> findByStatus(AppointmentStatus status);
    List<Appointment> findByPatient_NameContainingIgnoreCaseAndStatus(String patientName, AppointmentStatus status);
    List<Appointment> findByDoctor_NameContainingIgnoreCaseAndStatus(String doctorName, AppointmentStatus status);

    // Filter by date/time
    List<Appointment> findByAppointmentDateTimeAfter(LocalDateTime dateTime);
    List<Appointment> findByAppointmentDateTimeBefore(LocalDateTime dateTime);

    @Query("SELECT a FROM Appointment a WHERE a.appointmentDateTime BETWEEN :startDate AND :endDate")
    List<Appointment> findAppointmentsByDateRange(@Param("startDate") LocalDateTime startDate,
                                                  @Param("endDate") LocalDateTime endDate);

    // Delete old appointments
    @Modifying
    @Transactional
    @Query("DELETE FROM Appointment a WHERE a.appointmentDateTime < :date")
    void deleteAppointmentsOlderThan(@Param("date") LocalDateTime date);

    // Count by status
    long countByStatus(AppointmentStatus status);

    // Sorting
    List<Appointment> findAllByOrderByAppointmentDateTimeAsc();
    List<Appointment> findByPatient_NameContainingIgnoreCaseOrderByAppointmentDateTimeAsc(String patientName);
    List<Appointment> findByDoctor_NameContainingIgnoreCaseOrderByAppointmentDateTimeAsc(String doctorName);

    // Duplicate check
    boolean existsByDoctorAndAppointmentDateTime(Doctor doctor, LocalDateTime appointmentDateTime);

    // Filter by patient or doctor names (exact)
    List<Appointment> findByPatient_NameOrderByAppointmentDateTimeDesc(String patientName);
    List<Appointment> findByDoctor_NameOrderByAppointmentDateTimeAsc(String doctorName);

    // Filter by doctor ID
    List<Appointment> findByDoctor_Id(Long doctorId);
    List<Appointment> findByDoctor_IdAndStatus(Long doctorId, AppointmentStatus status);

    // Filter by doctor ID and patient name (partial)
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND LOWER(a.patient.name) LIKE LOWER(CONCAT('%', :patientName, '%'))")
    List<Appointment> findByDoctorIdAndPatientName(@Param("doctorId") Long doctorId, @Param("patientName") String patientName);

    // Filter by doctor ID and appointment date (date only)
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND FUNCTION('DATE', a.appointmentDateTime) = :date")
    List<Appointment> findByDoctorIdAndAppointmentDate(@Param("doctorId") Long doctorId, @Param("date") LocalDate date);
}
