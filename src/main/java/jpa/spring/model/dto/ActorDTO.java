package jpa.spring.model.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class ActorDTO {
    @NotBlank(message = "Name is mandatory")
    private String name;
    private Set<Long> movieIds;
    
}
