package com.appointment.bookingsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @NotBlank(message = "Target user is required")
    @Column(length = 20)
    private String targetUser; // Doctor / Staff / Patient

    @NotBlank
    @Size(max = 500)
    private String message;

    @NotBlank
    @Size(max = 50)
    private String type; // e.g., "Reminder", "Alert"

    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    @NotNull
    private Boolean readStatus = false;

    public Notification() {}

	public Notification(Long notificationId, @NotBlank(message = "Target user is required") String targetUser,
			@NotBlank @Size(max = 500) String message, @NotBlank @Size(max = 50) String type,
			@NotNull LocalDateTime createdAt, @NotNull Boolean readStatus) {
		super();
		this.notificationId = notificationId;
		this.targetUser = targetUser;
		this.message = message;
		this.type = type;
		this.createdAt = createdAt;
		this.readStatus = readStatus;
	}

	public Long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

	public String getTargetUser() {
		return targetUser;
	}

	public void setTargetUser(String targetUser) {
		this.targetUser = targetUser;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
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

