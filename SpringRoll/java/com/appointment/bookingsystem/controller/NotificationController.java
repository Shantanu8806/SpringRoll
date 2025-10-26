package com.appointment.bookingsystem.controller;

import com.appointment.bookingsystem.entity.Notification;
import com.appointment.bookingsystem.entity.Notification.TargetUser;
import com.appointment.bookingsystem.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // 1️⃣ Create a notification
    @PostMapping("/create")
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) {
        Notification created = notificationService.createNotification(notification);
        return ResponseEntity.ok(created);
    }

    // 2️⃣ Create admin delete request notification
    @PostMapping("/create/admin-delete-request")
    public ResponseEntity<Notification> createAdminDeleteRequestNotification(@RequestParam String message) {
        Notification created = notificationService.createAdminDeleteRequestNotification(message);
        return ResponseEntity.ok(created);
    }

    // 3️⃣ Get notification by ID
    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotification(@PathVariable Long id) {
        return notificationService.getNotificationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 4️⃣ Get all notifications
    @GetMapping("/all")
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationService.getAllNotifications();
        return notifications.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(notifications);
    }

    // 5️⃣ Get notifications by target user
    @GetMapping("/user/{targetUser}")
    public ResponseEntity<List<Notification>> getNotificationsByUser(@PathVariable TargetUser targetUser) {
        List<Notification> notifications = notificationService.getNotificationsByUser(targetUser);
        return notifications.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(notifications);
    }

    // 6️⃣ Get unread notifications for a user
    @GetMapping("/user/{targetUser}/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable TargetUser targetUser) {
        List<Notification> notifications = notificationService.getUnreadNotifications(targetUser);
        return notifications.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(notifications);
    }

    // 7️⃣ Mark a notification as read
    @PutMapping("/{id}/read")
    public ResponseEntity<Notification> markAsRead(@PathVariable Long id) {
        try {
            Notification updated = notificationService.markAsRead(id);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 8️⃣ Update a notification
    @PutMapping("/update")
    public ResponseEntity<Notification> updateNotification(@RequestBody Notification notification) {
        try {
            Notification updated = notificationService.updateNotification(notification);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 9️⃣ Delete a notification
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        try {
            notificationService.deleteNotification(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
