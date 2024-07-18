package jpa.spring.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import jpa.spring.core.ResponseObject;
import jpa.spring.model.dto.MovieDTO;
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
@RequestMapping("/api")
public class MovieController {

    @Autowired
    private final MovieService movieService;

    @GetMapping("/user/movies/getAllMovie")
     @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ResponseObject<List<MovieDTO>>> getAllMovies() {
        ResponseObject<List<MovieDTO>> responseObject = new ResponseObject<>();
        List<MovieDTO> movies = movieService.getAllMovies();
        responseObject.setMessage("Movies retrieved successfully");
        responseObject.setData(movies);
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }

    @GetMapping("/user/movies/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ResponseObject<Movie>> getMovieById(@PathVariable Long id) {
        Optional<Movie> movie = movieService.getMovieById(id);
        ResponseObject<Movie> result = new ResponseObject<>();
        result.setMessage("Movies get by id successfully");
        result.setData(movie.get());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/admin/movies/addMovie")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject<Movie>> addMovie(@RequestBody Movie movie) {
        ResponseObject<Movie> result = new ResponseObject<>();
        Movie newMovie = movieService.addMovie(movie);
            result.setMessage("Create a new Movie successfully");
            result.setData(newMovie);
            return new ResponseEntity<ResponseObject<Movie>>(result, HttpStatus.OK);
    }

    @PutMapping("/admin/movies/edit/{movieId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject<MovieDTO>> editMovie(@PathVariable("movieId") Long movieId,
            @RequestBody Movie movie) {
        ResponseObject<MovieDTO> result = new ResponseObject<>();
        MovieDTO editedMovieDTO = movieService.editMovie(movieId, movie);
        result.setMessage("Change Movie successfully");
        result.setData(editedMovieDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    @DeleteMapping("admin/movies/deleteMovie/{movieId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject<Void>> deleteMovie(@PathVariable("movieId") Long movieId) throws Exception {
        ResponseObject<Void> result = new ResponseObject<>();
        movieService.deleteMovie(movieId);
        result.setMessage("Delete Movie successfully");
        return new ResponseEntity<ResponseObject<Void>>(result, HttpStatus.OK);
    }

}
