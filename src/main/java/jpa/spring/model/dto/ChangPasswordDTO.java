package jpa.spring.model.dto;

import jpa.spring.config.validation.StrongPassword;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class ChangPasswordDTO {
    private String username;

    @StrongPassword
    private String newPassword;

}