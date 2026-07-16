package com.library.service;

import com.library.dto.NotificationDTO;
import com.library.entity.NotificationType;
import java.util.List;

public interface NotificationService {
    NotificationDTO createNotification(Long userId, String message, NotificationType type);
    List<NotificationDTO> getUserNotifications(Long userId);
    NotificationDTO markAsRead(Long notificationId);
}
