package jpa.spring.model.entities;

import java.time.ZonedDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.servlet.http.HttpSessionBindingEvent;
import jakarta.servlet.http.HttpSessionBindingListener;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
public class User implements HttpSessionBindingListener {
    @Id
    private String username;
    private String email;
    private String password;
    private String role;
    
    private ZonedDateTime uTimestmap;

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
