package jpa.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jpa.spring.model.entities.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
}
