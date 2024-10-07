package jpa.spring.config.security;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jpa.spring.model.dto.SignInDTO;
import jpa.spring.service.OAuth2AuthenticationService;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomOAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    
    private final OAuth2AuthenticationService userService; 

    @SuppressWarnings("deprecation")
    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        SignInDTO signInDTO = userService.handleOAuth2Authentication(authentication);

        ResponseEntity<SignInDTO> responseEntity = ResponseEntity
                .status(HttpStatus.OK)
                .body(signInDTO);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(responseEntity.getStatusCodeValue());
        response.getWriter().write("{\"username\": \"" + responseEntity.getBody().getUsername() + "\","
                                    + "\"email\": \"" + responseEntity.getBody().getEmail() + "\","
                                    + "\"image\": \"" + responseEntity.getBody().getImage() + "\","
                                    + "\"access_token\": \"" + responseEntity.getBody().getAccessToken() + "\","
                                    + "\"refresh_token\": \"" + responseEntity.getBody().getRefreshToken() + "\"}");
    }
}
