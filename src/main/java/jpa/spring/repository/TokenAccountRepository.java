package jpa.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jpa.spring.model.entities.TokenAccount;

import java.util.Optional;

public interface TokenAccountRepository extends JpaRepository<TokenAccount, Long>{
    Optional<TokenAccount> findByProvider(String Provide);
}
