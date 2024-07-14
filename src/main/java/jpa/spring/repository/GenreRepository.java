package jpa.spring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jpa.spring.model.entities.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    
    List<Genre> findByName(String name);

    Optional<Genre> findById(Long id);

    @Query("SELECT g FROM Genre g WHERE g.delFlag = false")
    List<Genre> findAllActiveGenres();

}
