package jpa.spring.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class SignInDTO {
    private String username;
    private String email;
    private String image;
    private String accessToken;
    private String refreshToken;

    public SignInDTO(String username, String email, String image, String accessToken, String refreshToken ) {
        this.username = username;
        this.email = email;
        this.image = image;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
