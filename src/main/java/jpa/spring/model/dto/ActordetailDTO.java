package jpa.spring.model.dto;

import java.util.Set;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class ActordetailDTO {
    private Long actorId;
    private String name;
    private Set<Long> movieIds;

}
