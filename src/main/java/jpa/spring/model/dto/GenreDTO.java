package jpa.spring.model.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@RequiredArgsConstructor
public class GenreDTO {
    @NotBlank(message = "Name is mandatory")
    private String name;
    private Set<Long> movieIds;
}
