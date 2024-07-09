package jpa.spring.model.Mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import jpa.spring.model.dto.ActordetailDTO;
import jpa.spring.model.entities.Actor;
import jpa.spring.model.entities.Movie;

@Mapper(componentModel = "spring")
public interface ActorMapper {
    ActorMapper INSTANCE = Mappers.getMapper(ActorMapper.class);

    @Mapping(target = "movieIds", source = "movies")
    ActordetailDTO actordetailDTO(Actor actor);
    
      default Set<Long> map(Set<Movie> movies) {
        return movies.stream()
                .map(Movie::getMovieId)
                .collect(Collectors.toSet());
    }

    




}
