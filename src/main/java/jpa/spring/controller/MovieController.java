package jpa.spring.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import jpa.spring.core.ResponseObject;
import jpa.spring.model.entities.Movie;
import jpa.spring.service.MovieService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private final MovieService movieService;

    @GetMapping("/getAllMovie")
    public ResponseObject<List<Movie>> getAllMovies() {
        ResponseObject<List<Movie>> responseObject = new ResponseObject<>();
        try {
            List<Movie> movies = movieService.getAllMovies();
            responseObject.setMessage("Movies retrieved successfully");
            responseObject.setData(movies);
        } catch (Exception e) {
            responseObject.setMessage("Failed to retrieve movies");

        }
        return responseObject;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Optional<Movie> movie = movieService.getMovieById(id);
        return movie.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping("/addMovie")
    public ResponseEntity<ResponseObject<Movie>> addMovie(@RequestBody Movie movie) {
        ResponseObject<Movie> result = new ResponseObject<>();
        try {
            Movie newMovie = movieService.addMovie(movie);
            result.setMessage("Create a new Movie successfully");
            result.setData(newMovie);
            return new ResponseEntity<ResponseObject<Movie>>(result, HttpStatus.OK);
        } catch (Exception e) {
            result.setMessage("Failed to create a new Movie: " + e.getMessage());
            result.setData(null);
            return new ResponseEntity<ResponseObject<Movie>>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/edit/{movieId}")
    public ResponseEntity<ResponseObject<Movie>> editMovie(@PathVariable("movieId") Long movieId,
            @RequestBody Movie movie) {
        ResponseObject<Movie> result = new ResponseObject<>();
        try {
            Movie editedMovie = movieService.editMovie(movieId, movie);
            result.setMessage("Change Movie successfully");
            result.setData(editedMovie);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            result.setMessage("Change Movie failed: " + e.getMessage());
            result.setData(null);
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteMovie/{movieId}")
    public ResponseEntity<ResponseObject<Void>> deleteMovie(@PathVariable("movieId") Long movieId) throws Exception {
        ResponseObject<Void> result = new ResponseObject<>();
        try {
            movieService.deleteMovie(movieId);
            result.setMessage("Delete Movie successfully");
            return new ResponseEntity<ResponseObject<Void>>(result, HttpStatus.OK);
        } catch (Exception e) {
            result.setMessage("Delete Movie failed: " + e.getMessage());
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

}
