package jpa.spring.service;

import java.time.ZonedDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jpa.spring.config.exception.UnknowException;
import jpa.spring.model.entities.Notification;
import jpa.spring.model.entities.User;
import jpa.spring.repository.NotificationRepository;
import jpa.spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

    @Autowired
    private final NotificationRepository notificationRepository;

    @Autowired
    private final FCMService fcmService;

    @Autowired
    private final UserRepository userRepository;

    public void createAndSendNotification(User owner, String message) {
        Notification notification = new Notification();
        notification.setOwner(owner);
        notification.setMessage(message);
        notification.setIsRead(false);
        notification.setDelFlag(false);
        notification.setCreatedAt(ZonedDateTime.now());

        notificationRepository.save(notification);

        Long userId = owner.getUserId();
        if (userId != null) {
            fcmService.sendNotificationToUser(userId, "New Notification", message); // Sửa ở đây
        } else {
            System.out.println("No user ID found for user: " + owner.getUsername());
        }
    }

    public Notification markNotificationAsRead(Long notificationId) {
        Optional<Notification> optional = notificationRepository.findById(notificationId);
        if (!optional.isPresent()) {
            throw new UnknowException("Notification with ID " + notificationId + " does not exist.");
        }
        Notification notification = optional.get();
        notification.setIsRead(true);
        return notificationRepository.save(notification); // Đảm bảo lưu lại trạng thái đã đọc
    }

    public void deleteNotification(Long notificationId) {
        Optional<Notification> optional = notificationRepository.findById(notificationId);
        if (!optional.isPresent()) {
            throw new UnknowException("Notification with ID " + notificationId + " does not exist.");
        }
        Notification notification = optional.get();
        notification.setDelFlag(true);
        notificationRepository.save(notification);
    }

    public void updateFcmToken(String tokenFcm, String username) {
        userRepository.deleteTokenByFcmToken(tokenFcm);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UnknowException("Không tìm thấy người dùng"));
        user.setFcmToken(tokenFcm);
        userRepository.save(user);
    }

    public void deleteFcmToken(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UnknowException("Không tìm thấy người dùng"));
        user.setFcmToken("");
        userRepository.save(user);
    }
}
