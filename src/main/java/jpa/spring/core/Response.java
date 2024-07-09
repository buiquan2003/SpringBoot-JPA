package jpa.spring.core;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class Response {
    private String status;
    private String message;
    private User data;
}
