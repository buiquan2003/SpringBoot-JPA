package jpa.spring.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jpa.spring.config.exception.UnknowException;
import jpa.spring.model.Mapper.MovieMapper;
import jpa.spring.model.dto.MovieDTO;
import jpa.spring.model.entities.Actor;
import jpa.spring.model.entities.Genre;
import jpa.spring.model.entities.Movie;
import jpa.spring.repository.ActorRepository;
import jpa.spring.repository.GenreRepository;
import jpa.spring.repository.MovieRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MovieService {

    @Autowired
    private final MovieRepository movieRepository;

    @Autowired
    private final ActorRepository actorRepository;

    @Autowired
    private final GenreRepository genreRepository;


    @Autowired
    private final MovieMapper movieMapper;

    public List<MovieDTO> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        return movies.stream()
                .map(movieMapper::movieToMovieDTO)
                .collect(Collectors.toList());
    }

    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }

    public Movie addMovie(Movie movie) {
        Optional<Movie> movieWithTitleOpt = movieRepository.findBytitle(movie.getTitle());
        if (movieWithTitleOpt.isPresent()) {
            throw new UnknowException("Movie with title \"" + movie.getTitle() + "\" already exists.");
        }
        Set<Actor> actors = movie.getActors();
        for (Actor actor : actors) {
            if (!actorRepository.existsById(actor.getActorId())) {
                throw new UnknowException("Actor with ID \"" + actor.getActorId() + "\" does not exist.");
            }
        }

        Set<Genre> genres = movie.getGenres();
        for (Genre genre : genres) {
            if (!genreRepository.existsById(genre.getGenreId())) {
                throw new UnknowException("Actor with ID \"" + genre.getGenreId() + "\" does not exist.");
            }
        }

        movie.setCreatedAt(ZonedDateTime.now());
        movie.setDelFlag(false);
        movieRepository.save(movie);
        return movie;
    }

    
    public MovieDTO editMovie(Long movieId, Movie movie) {
    
        Optional<Movie> existingMovieOpt = movieRepository.findById(movieId);
        if (!existingMovieOpt.isPresent()) {
            throw new UnknowException("Movie with ID " + movieId + " does not exist.");
        }

        Movie existingMovie = existingMovieOpt.get();
        existingMovie.setTitle(movie.getTitle());
        existingMovie.setDescription(movie.getDescription());
        existingMovie.setGenre(movie.getGenre());
        existingMovie.setReleaseYear(movie.getReleaseYear());
        existingMovie.setDirector(movie.getDirector());
        existingMovie.setCast(movie.getCast());
        existingMovie.setPosterUrl(movie.getPosterUrl());
        existingMovie.setTrailerUrl(movie.getTrailerUrl());
        existingMovie.setCreatedAt(ZonedDateTime.now());
        
        Set<Actor> actors = movie.getActors();
        for (Actor actor : actors) {
            if (!actorRepository.existsById(actor.getActorId())) {
                throw new UnknowException("Actor with ID \"" + actor.getActorId() + "\" does not exist.");
            }
        }

        Set<Genre> genres = movie.getGenres();
        for (Genre genre : genres) {
            if (!actorRepository.existsById(genre.getGenreId())) {
                throw new UnknowException("Actor with ID \"" + genre.getGenreId() + "\" does not exist.");
            }
        }
        existingMovie.setActors(actors);
        existingMovie.setGenres(genres);

        Movie savedMovie = movieRepository.save(existingMovie);
        return movieMapper.movieToMovieDTO(savedMovie);
    }

    public void deleteMovie(Long movieId) throws Exception {
        Movie existingMovie = movieRepository.findById(movieId)
                .orElseThrow(() -> new UnknowException("Movie with ID " + movieId + " does not exist."));
        existingMovie.setDelFlag(true);
        movieRepository.save(existingMovie);
    }

}
