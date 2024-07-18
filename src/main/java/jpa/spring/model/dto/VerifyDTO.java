package jpa.spring.model.dto;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
public class VerifyDTO {
    private String phoneNumber;
    private String otp;
}
