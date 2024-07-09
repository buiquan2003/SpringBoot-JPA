package jpa.spring.model.dto;

import java.util.Set;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class MovieDTO {
    private Long movieId;
    private String title;
    private String description;
    private String genre;
    private String releaseYear;
    private String director;
    private String cast;
    private String posterUrl;
    private String trailerUrl;
    private Set<Long> actorIds;
    private Set<Long> genreIds;
}
