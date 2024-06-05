package jpa.spring.config;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@Component
public class wedSession implements  HttpSessionListener {

    private static int numberSesstion = 0;

   @Override
   public void sessionCreated(HttpSessionEvent se) {
    se.getSession().getServletContext().log("add session : " +  ++numberSesstion);
}    

   @Override
   public void sessionDestroyed(HttpSessionEvent se) {
    se.getSession().getServletContext().log("add session : " + --numberSesstion);
   }
}
