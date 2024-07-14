package jpa.spring.service;

import java.time.ZonedDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jpa.spring.config.exception.UnknowException;
import jpa.spring.model.entities.Notification;
import jpa.spring.model.entities.User;
import jpa.spring.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

    @Autowired
    private final NotificationRepository notificationRepository;

    @Autowired
    private final FCMService fcmService;

    public void createAndSendNotification(User owner, String message) {
        Notification notification = new Notification();
        notification.setOwner(owner);
        notification.setMessage(message);
        notification.setRead(false);
        notification.setDelFlag(false);
        notification.setCreatedAt(ZonedDateTime.now());

        notificationRepository.save(notification);

        fcmService.sendNotification(owner.getFcmToken(), "New Notification", message);
    }

    public Notification markNotificationAsRead(Long notificationId) {
        Optional<Notification> optional = notificationRepository.findById(notificationId);
        if (!optional.isPresent()) {
            throw new UnknowException("Notification with ID " + optional + " does not exist.");

        }
        Notification notification = optional.get();
        notification.setRead(true);
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
