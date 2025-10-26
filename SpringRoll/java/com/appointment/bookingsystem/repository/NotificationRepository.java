package com.appointment.bookingsystem.repository;

import com.appointment.bookingsystem.entity.Notification;
import com.appointment.bookingsystem.entity.Notification.TargetUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByTargetUser(TargetUser targetUser);

    List<Notification> findByTargetUserAndReadStatusFalse(TargetUser targetUser);
}
