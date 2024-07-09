package jpa.spring.model.dto;

import java.time.ZonedDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class WatchHistoryDTO {
    private Long historyId;
    private Long ownerId; 
    private Long movieId; 
    private Boolean delFlag;
    private ZonedDateTime createdAt;
}
