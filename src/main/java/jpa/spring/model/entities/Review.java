package jpa.spring.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_account_id")
    @JsonBackReference
    private User owner;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    @JsonBackReference
    private Movie movie;

    @Column(nullable = false)
    private Integer rating;

    private Boolean delFlag;
    private String comment;
    private ZonedDateTime createdAt = ZonedDateTime.now();

}
