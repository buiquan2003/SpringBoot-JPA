package jpa.spring.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import jpa.spring.core.ResponseObject;
import jpa.spring.model.dto.MovieFileDTO;
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
        ResponseObject<List<MovieFile>> result = new ResponseObject<>();
        List<MovieFile> movieFiles = movieFileService.getMovieFilesByMovieId(movieId);
        result.setMessage("get MovieFiles Success");
        result.setData(movieFiles);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @GetMapping("/{fileId}")
    public ResponseEntity<ResponseObject<MovieFile>> getMovieFileById(@PathVariable Long fileId) {
        ResponseObject<MovieFile> result = new ResponseObject<>();
        Optional<MovieFile> movieFileOptional = movieFileService.getMovieFileById(fileId);
        result.setMessage("get MovieFile Success");
        result.setData(movieFileOptional.get());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/movie/{movieId}")
    public ResponseEntity<ResponseObject<MovieFileDTO>> addMovieFile(@PathVariable("movieId") Long movieId,
            @RequestBody MovieFile movieFile) {
        ResponseObject<MovieFileDTO> result = new ResponseObject<>();
        MovieFileDTO newMovieFile = movieFileService.addMovieFile(movieId, movieFile);

        result.setMessage("add MovieFile Success");
        result.setData(newMovieFile);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{fileId}")
    public ResponseEntity<ResponseObject<MovieFileDTO>> updateMovieFile(@PathVariable("fileId") Long fileId,
            @RequestBody MovieFile movieFileDetails)
            throws Exception {
        ResponseObject<MovieFileDTO> result = new ResponseObject<>();
        MovieFileDTO updatedMovieFile = movieFileService.updateMovieFile(fileId, movieFileDetails);

        result.setMessage("update MovieFile Success");
        result.setData(updatedMovieFile);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<ResponseObject<Void>> deleteMovieFile(@PathVariable Long fileId) {
        ResponseObject<Void> result = new ResponseObject<>();
        movieFileService.deleteMovieFile(fileId);
        result.setMessage("deleteMovie MovieFile Success");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
