package jpa.spring.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import jpa.spring.model.entities.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
      Optional<Movie> findBytitle(String title);
      Optional<Movie> findById(Long id);
      List<Movie> findBydelFlagFalse();

      
}
