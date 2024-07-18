package jpa.spring.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import jpa.spring.model.dto.PhoneDTO;


@Service
public class TwilioService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String fromPhoneNumber;

    public void sendOtp(PhoneDTO phoneDTO) {
        Twilio.init(accountSid, authToken);
       Message.creator(
                new PhoneNumber(phoneDTO.getPhoneNumber()),
                new PhoneNumber(fromPhoneNumber),
                "Your OTP code is: " + phoneDTO.getOtp())
                .create();
    }
}
