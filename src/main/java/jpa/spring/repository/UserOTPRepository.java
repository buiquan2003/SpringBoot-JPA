package jpa.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jpa.spring.model.dto.UserOTPVerificationDTO;

public interface UserOTPRepository extends JpaRepository<UserOTPVerificationDTO, Long> {
    
}
