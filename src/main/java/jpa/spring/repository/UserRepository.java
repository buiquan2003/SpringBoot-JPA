package jpa.spring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import io.lettuce.core.dynamic.annotation.Param;
import jpa.spring.model.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

   Optional<User> findByUsername(String username);

   Optional<User> findByEmail(String email);

   Optional<User> findByPhone(String phone);

   Optional<User> findByFcmToken(String FMC);

   Optional<User> findByUserId(Long userId);

   @Modifying
   @Query("UPDATE User a SET a.fcmToken = NULL WHERE a.fcmToken = :fcmToken")
   void deleteTokenByFcmToken(@Param("tokenFcm") String fcmToken);
}
