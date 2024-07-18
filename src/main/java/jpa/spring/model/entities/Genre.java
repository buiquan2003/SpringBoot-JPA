package jpa.spring.model.entities;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@RequiredArgsConstructor
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long genreId;

    @NotBlank(message = "Name is mandatory")
    @Pattern(regexp = "^.{3,}$", message = "Content must be at least 3 characters long")
    private String name;

    @ManyToMany(mappedBy = "genres", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Movie> movies = new HashSet<>();

    private Boolean delFlag;

}
