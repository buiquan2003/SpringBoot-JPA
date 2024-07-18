package jpa.spring.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jpa.spring.repository.*;
import lombok.RequiredArgsConstructor;
import jpa.spring.config.exception.UnknowException;
import jpa.spring.config.exception.UserAccountExistingException;
import jpa.spring.model.Mapper.GenreMapper;
import jpa.spring.model.dto.*;
import jpa.spring.model.entities.*;

@RequiredArgsConstructor
@Service
public class GennerService {

    @Autowired
    private final GenreRepository genreRepository;

     @Autowired
    private final MovieRepository movieRepository;

    @Autowired
    private final GenreMapper genreMapper;

    public List<GenreDetailDTO> getAllGenres() {
        List<Genre> genres = genreRepository.findAll();
        return genres.stream().map(genreMapper::genreDTO).collect(Collectors.toList());
    }

    public Optional<Genre> getGenreById(Long id) {
        return genreRepository.findById(id);
    }

    public List<Genre> getAllGenresTrue() {
        return genreRepository.findAllActiveGenres();
    }

    public GenreDetailDTO createGenre(Genre genre) {
     Optional<Genre> id = genreRepository.findBygenreId(genre.getGenreId());
        if (id.isPresent()) {
            throw new UserAccountExistingException(
                    "id " + id + " already exist. Please try an other!");
        }
        Set<Movie> movies = genre.getMovies();
        for (Movie movie : movies) {
            if (!movieRepository.existsById(movie.getMovieId())) {
                throw new UnknowException("Actor with ID \"" + movie.getMovieId() + "\" does not exist.");
            }
        }

        Genre genre2 = genreRepository.save(genre);
        return genreMapper.genreDTO(genre2);
    }

    public Genre updatGenre(Long id, Genre genreDetails) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Genre not found"));
        genre.setName(genreDetails.getName());
        return genreRepository.save(genre);
    }


    public Genre deleteGenre(Long id) {
        Genre GenreMovie = genreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Genre not found"));
        GenreMovie.setDelFlag(true);
        return genreRepository.save(GenreMovie);

    }

}
