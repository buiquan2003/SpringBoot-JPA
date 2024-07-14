package jpa.spring.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jpa.spring.config.exception.*;
import jpa.spring.model.Mapper.ActorMapper;
import jpa.spring.model.dto.*;
import jpa.spring.model.entities.*;
import jpa.spring.repository.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ActorService {

    @Autowired
    private final ActorRepository actorRepository;

    @Autowired
    private final MovieRepository movieRepository;

    @Autowired
    private final ActorMapper actorMapper;

    public List<ActordetailDTO> getAllActor() {
        List<Actor> actors = actorRepository.findAll();
        return actors.stream().map(actorMapper::actordetailDTO).collect(Collectors.toList());
    }

    public Optional<Actor> getbyId(Long id) {
        return actorRepository.findById(id);
    }

    public ActordetailDTO createActor(Actor actor) {
        
        Optional<Actor> id = actorRepository.findByActorId(actor.getActorId());
        if (id.isPresent()) {
            throw new UserAccountExistingException(
                    "id " + id + " already exist. Please try an other!");
        }
        Set<Movie> movies = actor.getMovies();
        for (Movie movie : movies) {
            if (!movieRepository.existsById(movie.getMovieId())) {
                throw new UnknowException("Actor with ID \"" + movie.getMovieId() + "\" does not exist.");
            }
        }

        Actor actor2 = actorRepository.save(actor);
        return actorMapper.actordetailDTO(actor2);
    }

    public ActordetailDTO updateActor(Long id, Actor actor) {
        Optional<Actor> idOptional = actorRepository.findById(id);
        if (idOptional.isPresent()) {
            throw new UserAccountExistingException("id " + id + "Already exist. Please try an other");
        }
        Actor actor2 = actorRepository.save(actor);
        return actorMapper.actordetailDTO(actor2);

    }

    public void deleteActor(Long id) {
        Actor actor = actorRepository.findById(id)
                .orElseThrow(() -> new UnknowException("Movie with ID " + id + " does not exist."));
        actor.setDelFlag(true);
        actorRepository.save(actor);
    }

    public List<Actor> getActorsWithMovies() {
        return actorRepository.findAll().stream()
                .filter(actor -> actor.getMovies() != null && !actor.getMovies().isEmpty())
                .collect(Collectors.toList());
    }

}
