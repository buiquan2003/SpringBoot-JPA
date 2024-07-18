package jpa.spring.model.entities;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.servlet.http.HttpSessionBindingEvent;
import jakarta.servlet.http.HttpSessionBindingListener;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jpa.spring.config.validation.StrongPassword;
import jpa.spring.config.validation.UserAccountElement;
import jpa.spring.core.Constant.UserAccountRegex;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@UserAccountElement.List({
        @UserAccountElement(field = "username", regex = UserAccountRegex.USERNAME, message = "username"),
        @UserAccountElement(field = "email", regex = UserAccountRegex.EMAIL, message = "email"),
})
public class User implements HttpSessionBindingListener {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank(message = "Username cannot be blank")
    private String username;

    private String email;

    @StrongPassword(message = "password")
    private String password;

    private String role;

    private ZonedDateTime uTimestmap;

    @Column(name = "fcm_token")
    private String fcmToken;

    private String googleId;

    private String authType = "email";

    private boolean verified;

    private String address;

    private String phone;

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

}
