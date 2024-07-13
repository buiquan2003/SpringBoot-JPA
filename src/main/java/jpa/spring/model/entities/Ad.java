package jpa.spring.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    
    @NotBlank(message = "Title is mandatory")
    @Pattern(regexp = "^.{3,}$", message = "Content must be at least 3 characters long")
    private String title;

    @NotBlank(message = "Content is mandatory")
    @Pattern(regexp = "^.{3,}$", message = "Content must be at least 3 characters long")
    private String content;
    
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ZonedDateTime createdAt = ZonedDateTime.now();
    private Boolean delFlag;
}
