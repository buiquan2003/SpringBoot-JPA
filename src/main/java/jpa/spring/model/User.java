package jpa.spring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.servlet.http.HttpSessionBindingEvent;
import jakarta.servlet.http.HttpSessionBindingListener;
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
    private long id;
    private String username;
    private String email;
    private String password;

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
