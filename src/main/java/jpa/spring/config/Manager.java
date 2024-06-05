package jpa.spring.config;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

public class Manager implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

   @Override
   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
           throws IOException, ServletException {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
        String uri = httpRequest.getRequestURI();
        System.out.println("Request URI: " + uri);

        // Chuyển tiếp yêu cầu đến filter hoặc servlet tiếp theo
        chain.doFilter(request, response);

        System.out.println("Response completed for URI: " + uri);
   }
    
   @Override
   public void destroy() {
       Filter.super.destroy();
   }



     
    
}
