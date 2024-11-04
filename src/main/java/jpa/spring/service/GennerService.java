package jpa.spring.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jpa.spring.repository.*;
import lombok.RequiredArgsConstructor;
import jpa.spring.config.exception.*;
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
    private final BaseRedisService baseRedisService;

    @Autowired
    private final GenreMapper genreMapper;

    private static final String PRODUCT_KEY_PREFIX = "Gender:"; 

    public List<GenreDetailDTO> getAllGenres() {
     
        @SuppressWarnings("unchecked")
        List<Genre> genres = (List<Genre>) baseRedisService.hashGet(PRODUCT_KEY_PREFIX, "genres");

        if (genres == null || genres.isEmpty()) {
             genres = genreRepository.findAll();
            baseRedisService.hashSet(PRODUCT_KEY_PREFIX, "genres", genres);
        }

        return genres.stream().map(genreMapper::genreDTO).collect(Collectors.toList());
    }

    public Optional<Genre> getGenreById(Long genreId) {
        String id = String.valueOf(genreId);
        @SuppressWarnings("unchecked")
        Optional<Genre> genreFromCache = (Optional<Genre>) baseRedisService.hashGet(PRODUCT_KEY_PREFIX, id);
        
        if (genreFromCache.isPresent()) {
            return genreFromCache; 
        } else {
            Optional<Genre> genreFromDb = genreRepository.findById(genreId);
            genreFromDb.ifPresent(genre -> baseRedisService.hashSet(PRODUCT_KEY_PREFIX, id, genre));
            return genreFromDb;
        }
    }

    public List<Genre> getAllGenresTrue() {
        return genreRepository.findAllActiveGenres();
    }

    public GenreDetailDTO createGenre(Genre genre) {
        Optional<Genre> existingGenre = genreRepository.findBygenreId(genre.getGenreId());
        if (existingGenre.isPresent()) {
            throw new UserAccountExistingException("id " + genre.getGenreId() + " already exists. Please try another!");
        }

        Set<Movie> movies = genre.getMovies();
        for (Movie movie : movies) {
            if (!movieRepository.existsById(movie.getMovieId())) {
                throw new UnknowException("Movie with ID \"" + movie.getMovieId() + "\" does not exist.");
            }
        }

        Genre savedGenre = genreRepository.save(genre);
        
        List<Genre> genres = genreRepository.findAll(); 
        baseRedisService.hashSet(PRODUCT_KEY_PREFIX, "genres", genres);
        
        return genreMapper.genreDTO(savedGenre);
    }
    
    public Genre updateGenre(Long id, Genre genreDetails) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Genre not found"));
        genre.setName(genreDetails.getName());
        Genre updatedGenre = genreRepository.save(genre);

        baseRedisService.hashSet(PRODUCT_KEY_PREFIX, String.valueOf(updatedGenre.getGenreId()), updatedGenre);
        
        return updatedGenre;
    }

    public Genre deleteGenre(Long id) {
        Genre genreToDelete = genreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Genre not found"));
        genreToDelete.setDelFlag(true);
        Genre deletedGenre = genreRepository.save(genreToDelete);
        
        List<Genre> genres = genreRepository.findAll();
        baseRedisService.hashSet(PRODUCT_KEY_PREFIX, "genres", genres);

        return deletedGenre;
    }
}
