package com.appointment.bookingsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    public enum TargetUser {
        DOCTOR,
        STAFF,
        PATIENT,
        ADMIN   // added for admin notifications
    }

    public enum NotificationType {
        REMINDER,
        ALERT,
        INFO,
        DELETE_REQUEST   // added for deletion requests
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @NotNull(message = "Target user is required")
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private TargetUser targetUser;

    @NotBlank
    @Size(max = 500)
    private String message;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private NotificationType type;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private Boolean readStatus;

    public Notification() {}

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (readStatus == null) readStatus = false;
    }

    // Full constructor
    public Notification(Long notificationId, TargetUser targetUser, String message, NotificationType type,
                        LocalDateTime createdAt, Boolean readStatus) {
        this.notificationId = notificationId;
        this.targetUser = targetUser;
        this.message = message;
        this.type = type;
        this.createdAt = createdAt;
        this.readStatus = readStatus;
    }

    // Getters and Setters
    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public TargetUser getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(TargetUser targetUser) {
        this.targetUser = targetUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Boolean readStatus) {
        this.readStatus = readStatus;
    }
}
