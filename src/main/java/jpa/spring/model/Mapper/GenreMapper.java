package jpa.spring.model.Mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import jpa.spring.model.dto.GenreDetailDTO;
import jpa.spring.model.entities.Genre;
import jpa.spring.model.entities.Movie;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    GenreMapper INSTANCE = Mappers.getMapper(GenreMapper.class);

    @Mapping(target = "movieIds", source = "movies")
    GenreDetailDTO genreDTO(Genre genre);

    default Set<Long> map(Set<Movie> movies) {
        return movies.stream()
                .map(Movie::getMovieId)
                .collect(Collectors.toSet());
    }

}