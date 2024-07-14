package jpa.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jpa.spring.model.entities.Ad;

public interface AdRepository extends JpaRepository<Ad, Long> {
    
}
