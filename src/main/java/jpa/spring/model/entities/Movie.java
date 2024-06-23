package jpa.spring.model.entities;

import java.time.ZonedDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jpa.spring.core.Constant.MovieRegex;
import jpa.spring.model.dto.BaseDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "movies")
@Getter
@Setter
@RequiredArgsConstructor

public class Movie extends BaseDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId;

    @NotBlank(message = "Title is mandatory")
    @Pattern(regexp = MovieRegex.TITLE, message = "Invalid title")
    private String title;

    @NotBlank(message = "Description is mandatory")
    private String description;

    @NotBlank(message = "Genre is mandatory")
    private String genre;

    @NotNull(message = "Release year is mandatory")
    @Pattern(regexp = MovieRegex.RELEASEYEAR, message = "Invalid release year")
    private String releaseYear;

    @NotBlank(message = "Director is mandatory")
    @Pattern(regexp = MovieRegex.DIRECTOR, message = "Invalid director")
    private String director;

    @NotBlank(message = "Cast is mandatory")
    private String cast;

    @NotBlank(message = "Poster URL is mandatory")
    private String posterUrl;

    @NotBlank(message = "Trailer URL is mandatory")
    private String trailerUrl;
    private ZonedDateTime createdAt = ZonedDateTime.now();
    private Boolean delFlag;

}