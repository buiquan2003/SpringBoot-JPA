package jpa.spring.service;

import java.time.ZonedDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jpa.spring.config.exception.*;
import jpa.spring.model.entities.*;
import jpa.spring.repository.*;
import lombok.*;

@Service
@RequiredArgsConstructor
public class NotificationService {

    @Autowired
    private final NotificationRepository notificationRepository;

    @Autowired
    private final FCMService fcmService;

    public void createAndSendNotification(User owner, String message) {
        // Tạo đối tượng Notification và thiết lập các thuộc tính cần thiết
        Notification notification = new Notification();
        notification.setOwner(owner);
        notification.setMessage(message);
        notification.setIsRead(false);
        notification.setDelFlag(false);
        notification.setCreatedAt(ZonedDateTime.now());
        
        notificationRepository.save(notification);

        String userToken = owner.getFcmToken();  
        if (userToken != null && !userToken.isEmpty()) {
            fcmService.sendNotification(userToken, "New Notification", message);
        } else {
            System.out.println("No FCM token found for user: " + owner.getUsername());
        }
    }

    public Notification markNotificationAsRead(Long notificationId) {
        Optional<Notification> optional = notificationRepository.findById(notificationId);
        if (!optional.isPresent()) {
            throw new UnknowException("Notification with ID " + optional + " does not exist.");

        }
        Notification notification = optional.get();
        notification.setIsRead(true);
        return notification;
    }

    public void deleteNotification(Long notificationId) {
        Optional<Notification> optional = notificationRepository.findById(notificationId);
        if (!optional.isPresent()) {
            throw new UnknowException("Notification with ID " + optional + " does not exist.");

        }
        Notification notification = optional.get();
        notification.setDelFlag(true);
        notificationRepository.save(notification);

    }
}
