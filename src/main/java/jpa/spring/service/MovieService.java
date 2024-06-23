package jpa.spring.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jpa.spring.model.entities.Movie;
import jpa.spring.repository.MovieRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MovieService {

    @Autowired
    private final MovieRepository movieRepository;

    public List<Movie> getAllMovies() {
        return movieRepository.findBydelFlagFalse();
    }

    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }
    
    public Movie addMovie(Movie movie) throws Exception {
        Optional<Movie> movieWithTitleOpt = movieRepository.findBytitle(movie.getTitle());
        if (movieWithTitleOpt.isPresent()) {
            throw new Exception("Movie with title \"" + movie.getTitle() + "\" already exists.");
        }
       
        movie.setCreatedAt(ZonedDateTime.now());
        movie.setDelFlag(false);
        movieRepository.save(movie);
        return movie;
    }
    
    public Movie editMovie(Long movieId, Movie movie) throws Exception {
        Optional<Movie> existingMovieOpt = movieRepository.findById(movieId);
        if (!existingMovieOpt.isPresent()) {
            throw new Exception("Movie with ID " + movieId + " does not exist.");
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
        existingMovie.setDelFlag(false);
        return movieRepository.save(existingMovie);
    }

    public void deleteMovie(Long movieId) throws Exception {
        Movie existingMovie = movieRepository.findById(movieId).orElseThrow(() -> 
        new Exception("Movie with ID " + movieId + " does not exist.")
    );
    existingMovie.setDelFlag(true);
    movieRepository.save(existingMovie);
    }

    
}
