package jpa.spring.model.entities;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.servlet.http.HttpSessionBindingEvent;
import jakarta.servlet.http.HttpSessionBindingListener;
import jakarta.validation.constraints.NotNull;
import jpa.spring.config.validation.StrongPassword;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@RequiredArgsConstructor
@Getter
@Setter

public class User implements HttpSessionBindingListener {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String username;

    private String email;

    @StrongPassword(message = "password")
    private String password;

    private ZonedDateTime uTimestmap;

    @Column(name = "fcm_token")
    private String fcmToken;

    private String googleId;

    private String authType = "email";

    private boolean verified;

    private String address;

    private String phone;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @NotNull
    private boolean delFlag;

    public boolean isEnabled() {
        return true;
    }

    public boolean isTokenValid() {
        return true;
    }

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        event.getSession().getServletContext().log("add session" + getUsername());
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        event.getSession().getServletContext().log("remove session" + getUsername());
    }

    public Optional<User> map(Object object) {
        throw new UnsupportedOperationException("Unimplemented method 'map'");
    }

}
