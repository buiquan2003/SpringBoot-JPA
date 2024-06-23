package jpa.spring.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Setter
@Getter
@RequiredArgsConstructor
@Entity
@Table(name = "ads")
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adId;

    private String title;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ZonedDateTime createdAt = ZonedDateTime.now();
    private Boolean delFlag;
}
