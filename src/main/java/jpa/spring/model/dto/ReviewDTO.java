package jpa.spring.model.dto;

import java.time.ZonedDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ReviewDTO {
  private Long reviewId;
  private Long userId;
  private Long movieId;
  private Integer rating;
  private String comment;
  private ZonedDateTime createdAt;
}
