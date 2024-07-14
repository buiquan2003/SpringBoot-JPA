package jpa.spring.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Setter
@Getter
@RequiredArgsConstructor
@Entity
public class MovieFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    @JsonManagedReference
    private Movie movie;

    @Column(nullable = false)
    private String fileUrl;

    @Enumerated(EnumType.STRING)
    private VideoQuality quality;

    private Boolean delFlag;
    private ZonedDateTime createdAt = ZonedDateTime.now();
}

enum VideoQuality {
    _480P, _720P, _1080P, _4K
}

