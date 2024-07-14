package jpa.spring.model.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import jpa.spring.model.dto.MovieDTO;
import jpa.spring.model.entities.Actor;
import jpa.spring.model.entities.Genre;
import jpa.spring.model.entities.Movie;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);

    @Mapping(target = "actorIds", source = "actors")
    @Mapping(target = "genreIds", source = "genres")
    MovieDTO movieToMovieDTO(Movie movie);

    default Set<Long> mapActors(Set<Actor> actors) {
        return actors.stream()
                     .map(Actor::getActorId)
                     .collect(Collectors.toSet());
    }

    default Set<Long> mapGenres(Set<Genre> genres) {
        return genres.stream()
                     .map(Genre::getGenreId)
                     .collect(Collectors.toSet());
    }
}
