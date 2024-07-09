package jpa.spring.model.dto;

import java.time.ZonedDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class MovieFileDTO {
    private Long fileId;
    private Long movieId;
    private String fileUrl;
    private Boolean delFlag;
    private ZonedDateTime createdAt;
}

