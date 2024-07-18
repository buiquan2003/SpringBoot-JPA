package jpa.spring.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import jpa.spring.core.ResponseObject;
import jpa.spring.model.dto.ReviewDTO;
import jpa.spring.model.entities.Review;
import jpa.spring.service.ReviewService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    @Autowired
    private final ReviewService reviewService;

    @GetMapping("/user/reviews/GetAllReview")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<ResponseObject<ReviewDTO>> getall() {
        List<ReviewDTO> reviews = reviewService.getAllReviews();
        ResponseObject<ReviewDTO> result = new ResponseObject<>();
        result.setMessage("get all reviews");
        result.setDaList(reviews);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/user/reviews/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ResponseObject<Review>> getOne(@PathVariable Long id) {
        Optional<Review> review = reviewService.getbyReview(id);
        ResponseObject<Review> reult = new ResponseObject<>();
        reult.setMessage("get by review");
        reult.setData(review.get());
        return new ResponseEntity<>(reult, HttpStatus.OK);

    }

    @PostMapping("user/reviews/createReview/{movieId}/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ResponseObject<Review>> createReview(@PathVariable("movieId") Long movieId,
            @PathVariable("userId") Long userId, @RequestBody Review reviewDetails) {
        ResponseObject<Review> reuslt = new ResponseObject<>();
        Review review = reviewService.createReview(movieId, userId, reviewDetails);
        reuslt.setMessage("Create review succsesfully");
        reuslt.setData(review);
        return new ResponseEntity<>(reuslt, HttpStatus.OK);
    }

    @PutMapping("user/reviews/updateReview/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ResponseObject<Review>> updateReview(@PathVariable("reviewId") Long reviewId,
            @RequestBody Review reviewDetails) {
        ResponseObject<Review> reuslt = new ResponseObject<>();
        Review review = reviewService.updateReview(reviewId, reviewDetails);
        reuslt.setMessage("Create review succsesfully");
        reuslt.setData(review);
        return new ResponseEntity<>(reuslt, HttpStatus.OK);
    }

    @DeleteMapping("/user/reviews/{reviewId}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ResponseObject<Void>> deleteReview(@PathVariable Long reviewId) {
        ResponseObject<Void> result = new ResponseObject<>();
        reviewService.deleteReview(reviewId);
        result.setMessage("delete Success");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
