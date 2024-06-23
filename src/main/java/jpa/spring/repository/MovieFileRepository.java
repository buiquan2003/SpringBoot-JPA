package jpa.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jpa.spring.model.entities.MovieFile;

public interface MovieFileRepository extends JpaRepository<MovieFile, Long> {
    
      @Query("SELECT mf FROM MovieFile mf WHERE mf.movie.movieId = :movieId AND mf.delFlag = false")
    List<MovieFile> findByMovieIdAndDelFlagFalse(@Param("movieId") Long movieId);

    
}