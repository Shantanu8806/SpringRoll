package com.appointment.bookingsystem.services;

import com.appointment.bookingsystem.entity.Notification;
import com.appointment.bookingsystem.entity.Notification.TargetUser;
import com.appointment.bookingsystem.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    // Create a new notification
    public Notification createNotification(Notification notification) {
        if (notification.getCreatedAt() == null) {
            notification.setCreatedAt(LocalDateTime.now());
        }
        if (notification.getReadStatus() == null) {
            notification.setReadStatus(false);
        }
        return notificationRepository.save(notification);
    }

    // Create notification specifically for ADMIN (delete request)
    public Notification createAdminDeleteRequestNotification(String message) {
        Notification notification = new Notification(
                null,
                TargetUser.ADMIN,
                message,
                Notification.NotificationType.DELETE_REQUEST,
                LocalDateTime.now(),
                false
        );
        return createNotification(notification);
    }

    // Get notification by ID
    public Optional<Notification> getNotificationById(Long id) {
        return notificationRepository.findById(id);
    }

    // Get all notifications
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    // Get notifications by target user
    public List<Notification> getNotificationsByUser(TargetUser targetUser) {
        return notificationRepository.findByTargetUser(targetUser);
    }

    // Get unread notifications for a specific user
    public List<Notification> getUnreadNotifications(TargetUser targetUser) {
        return notificationRepository.findByTargetUserAndReadStatusFalse(targetUser);
    }

    // Mark notification as read
    public Notification markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        notification.setReadStatus(true);
        return notificationRepository.save(notification);
    }

    // Update a notification
    public Notification updateNotification(Notification notification) {
        if (notification.getNotificationId() == null || !notificationRepository.existsById(notification.getNotificationId())) {
            throw new RuntimeException("Notification does not exist");
        }
        return notificationRepository.save(notification);
    }

    // Delete a notification
    public void deleteNotification(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new RuntimeException("Notification not found with id: " + id);
        }
        notificationRepository.deleteById(id);
    }
}
