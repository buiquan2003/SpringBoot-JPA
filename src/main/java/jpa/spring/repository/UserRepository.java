package jpa.spring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jpa.spring.model.entities.User;


public interface UserRepository extends JpaRepository<User, Long> {
   //Optional một người truy cập
   Optional<User> findByUsername(String username);
   Optional<User> findByEmail(String email);
   Optional<User> findByPhone(String phone);

} 
