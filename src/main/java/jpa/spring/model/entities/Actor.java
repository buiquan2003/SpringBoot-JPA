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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long actorId;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @ManyToMany(mappedBy = "actors", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Movie> movies = new HashSet<>();
    
    private Boolean delFlag;

}
