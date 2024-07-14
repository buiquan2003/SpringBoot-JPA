package jpa.spring.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jpa.spring.config.exception.UnknowException;
import jpa.spring.config.exception.UserAccountExistingException;
import jpa.spring.model.entities.Movie;
import jpa.spring.model.entities.MovieFile;
import jpa.spring.repository.MovieFileRepository;
import jpa.spring.repository.MovieRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovieFileService {

    @Autowired
    private final MovieFileRepository fileService;

    @Autowired
    private MovieRepository movieRepository;

    public List<MovieFile> getMovieFilesByMovieId(Long movieId) {
        return fileService.findByMovieIdAndDelFlagFalse(movieId);

    }

    public Optional<MovieFile> getMovieFileById(Long fileId) {
        return fileService.findById(fileId);
    }

    public MovieFile addMovieFile(Long movieId, MovieFile movieFile) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new UnknowException("Movie with ID " + movieId + " does not exist."));
        movieFile.setMovie(movie);
        return fileService.save(movieFile);

    }

    public MovieFile updateMovieFile(Long fileId, MovieFile movieFileDetails) {
        Optional<MovieFile> movieFile = fileService.findById(fileId);
        if (!movieFile.isPresent()) {
                      throw new UserAccountExistingException(
                    "Username " + movieFile + " already exist. Please try an other!");
        }
        MovieFile file = movieFile.get();
        file.setFileUrl(movieFileDetails.getFileUrl());
        file.setQuality(movieFileDetails.getQuality());
        file.setDelFlag(movieFileDetails.getDelFlag());
        return fileService.save(file);
    }

    public void deleteMovieFile(Long movieFileId) {
        MovieFile movieFile = fileService.findById(movieFileId)
                .orElseThrow(() -> new UnknowException("Movie with ID " + movieFileId + " does not exist."));
        movieFile.setDelFlag(true);
        fileService.save(movieFile);
    }

}
