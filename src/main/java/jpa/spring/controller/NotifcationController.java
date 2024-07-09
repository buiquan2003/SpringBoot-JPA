package jpa.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jpa.spring.core.ResponseObject;
import jpa.spring.model.entities.Notification;
import jpa.spring.model.entities.User;
import jpa.spring.service.NotificationService;
import jpa.spring.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotifcationController {

    @Autowired
    private final NotificationService notificationService;

    @Autowired
    private final UserService userService;

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestParam Long ownerId, @RequestParam String message) {
        User owner = userService.getUserById(ownerId);

        if (owner == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        notificationService.createAndSendNotification(owner, message);
        return ResponseEntity.ok("Notification sent successfully.");
    }

    @PostMapping("/{id}")
    public ResponseEntity<ResponseObject<Notification>> markAsRead(Long notificationId) {
        ResponseObject<Notification> result = new ResponseObject<>();
        Notification notification = notificationService.markNotificationAsRead(notificationId);
        result.setMessage("mark success");
        result.setData(notification);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject<Notification>> delete(Long notificationId) {
        ResponseObject<Notification> result = new ResponseObject<>();
        notificationService.deleteNotification(notificationId);
        result.setMessage("mark success");
        return new ResponseEntity<>(result, HttpStatus.OK);

    }
}
