package jpa.spring.model.dto;

import java.time.ZonedDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class UserCertification {

    private String username;
    private String accessToken;
    private ZonedDateTime expiredTime;
    private String refreshToken;
    private ZonedDateTime refreshTime;
}
