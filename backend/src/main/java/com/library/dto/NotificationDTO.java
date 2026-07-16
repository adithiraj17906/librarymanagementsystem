package com.library.dto;

import com.library.entity.Notification;
import com.library.entity.NotificationType;
import java.time.LocalDateTime;

public class NotificationDTO {
    private Long id;
    private Long userId;
    private String message;
    private NotificationType type;
    private Boolean isRead;
    private LocalDateTime createdAt;

    // No-args constructor
    public NotificationDTO() {
    }

    // All-args constructor
    public NotificationDTO(Long id, Long userId, String message, NotificationType type, Boolean isRead, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.type = type;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static NotificationDTO fromEntity(Notification notification) {
        if (notification == null) return null;
        return NotificationDTO.builder()
                .id(notification.getId())
                .userId(notification.getUser() != null ? notification.getUser().getId() : null)
                .message(notification.getMessage())
                .type(notification.getType())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }

    // Manual Builder Pattern
    public static NotificationDTOBuilder builder() {
        return new NotificationDTOBuilder();
    }

    public static class NotificationDTOBuilder {
        private Long id;
        private Long userId;
        private String message;
        private NotificationType type;
        private Boolean isRead;
        private LocalDateTime createdAt;

        public NotificationDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public NotificationDTOBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public NotificationDTOBuilder message(String message) {
            this.message = message;
            return this;
        }

        public NotificationDTOBuilder type(NotificationType type) {
            this.type = type;
            return this;
        }

        public NotificationDTOBuilder isRead(Boolean isRead) {
            this.isRead = isRead;
            return this;
        }

        public NotificationDTOBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public NotificationDTO build() {
            return new NotificationDTO(id, userId, message, type, isRead, createdAt);
        }
    }
}
