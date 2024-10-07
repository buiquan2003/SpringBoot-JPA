package jpa.spring.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.time.ZonedDateTime;

@Setter
@Getter
@RequiredArgsConstructor
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_account_id")
    private User owner;

    private String message;

    @Column(name = "is_read")
    private Boolean isRead;

    private Boolean delFlag;
    private ZonedDateTime createdAt = ZonedDateTime.now();
}

