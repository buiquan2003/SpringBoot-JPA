package jpa.spring.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jpa.spring.config.exception.UnknowException;
import jpa.spring.model.Mapper.MovieFileMapper;
import jpa.spring.model.dto.MovieFileDTO;
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
    private final MovieRepository movieRepository;

    @Autowired
    private final MovieFileMapper fileMapper;

    public List<MovieFile> getMovieFilesByMovieId(Long movieId) {
        return fileService.findByMovieIdAndDelFlagFalse(movieId);

    }

    public Optional<MovieFile> getMovieFileById(Long fileId) {
        return fileService.findById(fileId);
    }

    public MovieFileDTO addMovieFile(Long movieId, MovieFile movieFile) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new UnknowException("Movie with ID " + movieId + " does not exist."));
        movieFile.setMovie(movie);
        MovieFile movieFile2 = fileService.save(movieFile);
        return fileMapper.movieFileToMovieFileDTO(movieFile2);

    }

    public MovieFileDTO updateMovieFile(Long movieFileId, MovieFile movieFile) {
        Optional<MovieFile> existingMovieFileOpt = fileService.findById(movieFileId);
        if (!existingMovieFileOpt.isPresent()) {
            throw new UnknowException("MovieFile with ID " + movieFileId + " does not exist.");
        }

        MovieFile existingMovieFile = existingMovieFileOpt.get();
        existingMovieFile.setFileUrl(movieFile.getFileUrl());
        existingMovieFile.setQuality(movieFile.getQuality());
        existingMovieFile.setDelFlag(movieFile.getDelFlag());
        existingMovieFile.setCreatedAt(ZonedDateTime.now());
        MovieFile updatedMovieFile = fileService.save(existingMovieFile);
        return fileMapper.movieFileToMovieFileDTO(updatedMovieFile);
    }

    public MovieFile deleteMovieFile(Long movieFileId) {
        MovieFile movieFile = fileService.findById(movieFileId)
                .orElseThrow(() -> new UnknowException("Movie with ID " + movieFileId + " does not exist."));
        movieFile.setDelFlag(true);
       return fileService.save(movieFile);
    }

}
