package jpa.spring.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.time.ZonedDateTime;


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
    private Movie movie;

    @NotBlank(message = "File URL is mandatory")
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

