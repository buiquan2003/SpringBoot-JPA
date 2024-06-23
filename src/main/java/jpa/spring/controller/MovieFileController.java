package jpa.spring.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import jpa.spring.core.ResponseObject;
import jpa.spring.model.entities.MovieFile;
import jpa.spring.service.MovieFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/moviefiles")

public class MovieFileController {

    @Autowired
    private final MovieFileService movieFileService;

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<ResponseObject<List<MovieFile>>> getMovieFilesByMovieId(@PathVariable Long movieId) {
        ResponseObject<List<MovieFile>> responseObject = new ResponseObject<>();

        List<MovieFile> movieFiles = movieFileService.getMovieFilesByMovieId(movieId);
        if (responseObject.getError() == null) {
            responseObject.setMessage("get MovieFiles Success");
            responseObject.setData(movieFiles);
            return ResponseEntity.ok(responseObject);
        } else {
            return ResponseEntity.status(404).body(responseObject);
        }
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<ResponseObject<MovieFile>> getMovieFileById(@PathVariable Long fileId) {
        ResponseObject<MovieFile> responseObject = new ResponseObject<>();
        Optional<MovieFile> movieFileOptional = movieFileService.getMovieFileById(fileId);

        if (movieFileOptional.isPresent()) {
            responseObject.setMessage("get MovieFile Success");
            responseObject.setData(movieFileOptional.get());
            return ResponseEntity.ok(responseObject);
        } else {
            responseObject.setMessage("Failed to get MovieFile");
         //   responseObject.setError("Movie file not found for the given file ID.");
            return ResponseEntity.status(404).body(responseObject);
        }
    }

    @PostMapping("/movie/{movieId}")
    public ResponseEntity<ResponseObject<MovieFile>> addMovieFile(@PathVariable("movieId") Long movieId, @RequestBody MovieFile movieFile) {
        ResponseObject<MovieFile> responseObject = new ResponseObject<>();
        MovieFile newMovieFile = movieFileService.addMovieFile(movieId, movieFile);
        if (newMovieFile != null ) {
            responseObject.setMessage("add MovieFile Success");
            responseObject.setData(newMovieFile);
            return ResponseEntity.status(201).body(responseObject); 
        } else {
            responseObject.setMessage("Failed to add MovieFile");
           // responseObject.setError("Movie not found for the given movie ID.");
            return ResponseEntity.status(404).body(responseObject);
        }
    }

    @PutMapping("/{fileId}")
    public ResponseEntity<ResponseObject<MovieFile>> updateMovieFile(@PathVariable("fileId") Long fileId, @RequestBody MovieFile movieFileDetails)
            throws Exception {
                ResponseObject<MovieFile> responseObject = new ResponseObject<>();
        MovieFile updatedMovieFile = movieFileService.updateMovieFile(fileId, movieFileDetails);
        if (updatedMovieFile != null) {
            responseObject.setMessage("update MovieFile Success");
            responseObject.setData(updatedMovieFile);
            return ResponseEntity.status(201).body(responseObject); 
        } else {
            responseObject.setMessage("Failed to add MovieFile");
            // responseObject.setError(200,"Movie not found for the given movie ID.");
             return ResponseEntity.status(404).body(responseObject);
        }
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteMovieFile(@PathVariable Long fileId) throws Exception {
        movieFileService.deleteMovieFile(fileId);
        return ResponseEntity.noContent().build();
    }

}
