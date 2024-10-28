package jpa.spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jpa.spring.config.exception.UnknowException;
import jpa.spring.config.exception.UserAccountExistingException;
import jpa.spring.model.dto.*;
import jpa.spring.model.entities.Movie;
import jpa.spring.model.entities.Review;
import jpa.spring.model.entities.User;
import jpa.spring.repository.MovieRepository;
import jpa.spring.repository.ReviewRepository;
import jpa.spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    @Autowired
    private final ReviewRepository reviewRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final MovieRepository movieRepository;

    public List<ReviewDTO> getAllReviews() {
        return reviewRepository.findAll().stream()
            .map(review -> {
                ReviewDTO dto = new ReviewDTO();
                dto.setReviewId(review.getReviewId());
                dto.setUserId(review.getOwner().getUserId());
                dto.setMovieId(review.getMovie().getMovieId());
                dto.setRating(review.getRating());
                dto.setComment(review.getComment());
                dto.setCreatedAt(review.getCreatedAt());
                return dto;
            })
            .collect(Collectors.toList());
    }
    
    public Optional<Review> getbyReview(Long id) {
        return reviewRepository.findById(id);
    }

    public Review createReview(Long movieId, Long userId, Review reviewDetails) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Movie> movieOptional = movieRepository.findById(movieId);
    
        if (userOptional.isPresent() && movieOptional.isPresent()) {
            Review reviewNew = new Review();
            reviewNew.setOwner(userOptional.get());
            reviewNew.setMovie(movieOptional.get());
            reviewNew.setRating(reviewDetails.getRating());
            reviewNew.setComment(reviewDetails.getComment());
            reviewNew.setDelFlag(reviewDetails.getDelFlag());
            return reviewRepository.save(reviewNew);
        } else {
            throw new RuntimeException("User or Movie not found");
        }
    }

    public Review updateReview(Long id, Review review) {
        Optional<Review> idOptional = reviewRepository.findById(id);
        if (idOptional.isPresent()) {
            throw new UserAccountExistingException("id " + id + "Already exist. Please try an other");
        }
        return reviewRepository.save(review);
    }

    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new UnknowException("Movie with ID " + id + " does not exist."));
        review.setDelFlag(true);
        reviewRepository.save(review);
    }

    public List<Review> getActorsWithMovies() {
        return reviewRepository.findAll().stream()
                .filter(review -> review.getMovie() != null)
                .collect(Collectors.toList());
    }

}
