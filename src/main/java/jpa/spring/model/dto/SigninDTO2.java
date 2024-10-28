package jpa.spring.model.dto;

import jpa.spring.config.validation.StrongPassword;
import lombok.*;


@RequiredArgsConstructor
@Getter
@Setter


public class SigninDTO2 {
    
    private String username;
    @StrongPassword
    private String password;
}
