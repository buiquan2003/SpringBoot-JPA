package jpa.spring.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PhoneDTO {
    private String phoneNumber;
    private String otp;
    private String accessToken;
    private String refreshToken;
}
