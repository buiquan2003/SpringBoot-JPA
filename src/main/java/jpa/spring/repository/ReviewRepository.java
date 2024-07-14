package jpa.spring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jpa.spring.model.entities.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
          Optional<Review> findByreviewId(Long reviewId);
    
} 
