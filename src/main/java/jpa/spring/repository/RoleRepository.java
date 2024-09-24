package jpa.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jpa.spring.model.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
   Role findByName(String name);
    
} 
