package jpa.spring.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import jpa.spring.model.User;


public interface UserRepository extends JpaRepository<User, Integer> {
   List<User> findByUsername(String username);
} 
