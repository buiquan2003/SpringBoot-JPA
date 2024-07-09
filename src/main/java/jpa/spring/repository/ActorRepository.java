package jpa.spring.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jpa.spring.model.entities.Actor;

public interface ActorRepository extends JpaRepository<Actor, Long> {
    Optional<Actor> findByActorId(Long actorId);

    
} 
