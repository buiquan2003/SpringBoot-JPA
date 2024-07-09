package jpa.spring.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoriteId;

    @ManyToOne()
    @JoinColumn(name = "user_account_id")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    private Boolean delFlag;    
    private ZonedDateTime createdAt = ZonedDateTime.now();

}

