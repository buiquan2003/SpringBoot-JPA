package jpa.spring.service;

import com.google.firebase.messaging.*;

import jpa.spring.model.entities.User;
import jpa.spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FCMService {

    private final UserRepository userRepository; 
    public void sendFCMNotification(String fcmToken, String title, String body) {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(notification)
                .build();

      try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("FCM notification sent successfully: " + response);
        } catch (FirebaseMessagingException e) {
            System.err.println("Error sending FCM notification: " + e.getMessage());
        }
    }

    public void sendNotificationToUser(Long userId, String title, String body) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String fcmToken = user.getFcmToken();

            if (fcmToken != null && !fcmToken.isEmpty()) { // Kiểm tra token không null và không rỗng
                System.out.println("Sending notification to user: " + user);
                sendFCMNotification(fcmToken, title, body); // Sửa ở đây
            } else {
                System.out.println("No FCM token available for user: " + userId);
            }
        } else {
            System.out.println("User not found with ID: " + userId);
        }
    }
}
