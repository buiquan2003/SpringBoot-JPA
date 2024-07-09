package jpa.spring.model.Mapper;



import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import jpa.spring.model.dto.MovieFileDTO;
import jpa.spring.model.entities.MovieFile;


@Mapper(componentModel = "spring")
public interface MovieFileMapper {
    
 MovieFileMapper INSTANCE = Mappers.getMapper(MovieFileMapper.class);
 
 @Mapping(source = "movie.movieId", target = "movieId")
 MovieFileDTO movieFileToMovieFileDTO(MovieFile movieFile);

}
