package jpa.spring.model.dto;

import java.util.Set;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Setter
@Getter
public class GenreDetailDTO {
    private Long genreId;
    private String name;
    private Set<Long> movieIds;
}

