package jpa.spring.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
@Entity
public class TokenAccount {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long tokenId;

    private String username;

    private String accsessToken;
    private String provider;
    private String refreshToken;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User owner;

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
