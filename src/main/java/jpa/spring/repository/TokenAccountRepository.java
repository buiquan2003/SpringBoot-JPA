package jpa.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jpa.spring.model.entities.TokenAccount;

public interface TokenAccountRepository extends JpaRepository<TokenAccount, Long>{
    
}
