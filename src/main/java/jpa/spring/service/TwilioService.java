package jpa.spring.service;

import org.springframework.stereotype.Service;



@Service
public class TwilioService {

    // @Value("${twilio.account.sid}")
    // private String accountSid;

    // @Value("${twilio.auth.token}")
    // private String authToken;

    // @Value("${twilio.phone.number}")
    // private String fromPhoneNumber;

    // public void sendOtp(PhoneDTO phoneDTO) {
    //     Twilio.init(accountSid, authToken);
    //    Message.creator(
    //             new PhoneNumber(phoneDTO.getPhoneNumber()),
    //             new PhoneNumber(fromPhoneNumber),
    //             "Your OTP code is: " + phoneDTO.getOtp())
    //             .create();
    // }
}
