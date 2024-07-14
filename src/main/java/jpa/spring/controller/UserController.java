package jpa.spring.controller;

import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jpa.spring.config.utils.CookeiUtils;
import jpa.spring.config.utils.SessionUtil;
import jpa.spring.core.ResponseObject;
import jpa.spring.model.dto.PhoneDTO;
import jpa.spring.model.dto.UserCertification;
import jpa.spring.model.entities.User;
import jpa.spring.service.TwilioService;
import jpa.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final TwilioService twilioService;

    @GetMapping("/getAll")
    public ResponseEntity<ResponseObject<User>> getAllUser() {
        List<User> user = userService.getAllUser();
        ResponseObject<User> result = new ResponseObject<>();
        result.setDaList(user);
        result.setMessage("Get profile successfully");
        return new ResponseEntity<ResponseObject<User>>(result, HttpStatus.OK);

    }

    @PostMapping(path = "/signup")
    public ResponseEntity<ResponseObject<User>> register(@RequestBody @Valid User register) {
        ResponseObject<User> result = new ResponseObject<>();
        User user = userService.register(register);
        result.setMessage("Create a new account successfully");
        result.setData(user);
        return new ResponseEntity<ResponseObject<User>>(result, HttpStatus.OK);

    }

    @PostMapping("/signin")
    public ResponseEntity<ResponseObject<UserCertification>> authenticate(@RequestBody @Valid User user,
            HttpSession httpSession, HttpServletResponse response, HttpServletRequest request) {
        ResponseObject<UserCertification> result = new ResponseObject<>();
        try {
            UserCertification auth = userService.authetioncate(user);
            result.setMessage("sign in successfully");
            result.setData(auth);
            SessionUtil.setAttribute(request, "SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            String sessionId = httpSession.getId();
            CookeiUtils.create(response, "JSESSIONID", sessionId, 7 * 24 * 60 * 60, true, true);
            try {
                userService.sendOTPVerificationEmail(user.getUsername(), user.getEmail());
                System.out.println("OTP has been sent successfully.");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error sending OTP: " + e.getMessage());
            }
            return new ResponseEntity<ResponseObject<UserCertification>>(result, HttpStatus.OK);
        } catch (Exception e) {
            result.setMessage("sign in Failed");
            result.setData(null);
            return new ResponseEntity<ResponseObject<UserCertification>>(result, HttpStatus.BAD_REQUEST);
        }
    }
    


    @PostMapping("/signin/google")
    public ResponseEntity<?> signinWithGoogle(@RequestBody Map<String, String> body) {
        ResponseObject<User> result = new ResponseObject<>();
        Map<String, String> tokens = userService.signinWithGoogle(body);
        result.setMessage("succsee");
        return new ResponseEntity<>(tokens, HttpStatus.OK);

    }

    @PostMapping("/signin/phone")
    public ResponseEntity<?> signinWithPhone(@RequestBody PhoneDTO phoneDTO) {
        ResponseObject<PhoneDTO> result = new ResponseObject<>();
        try {
            Map<String, String> tokens = userService.registerAndSignInWithPhone(phoneDTO.getPhoneNumber(), phoneDTO.getOtp());
            phoneDTO.setAccessToken(tokens.get("accessToken"));
            phoneDTO.setRefreshToken(tokens.get("refreshToken"));
            result.setMessage("Sign in successfully");
            result.setData(phoneDTO);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            result.setMessage("Sign in failed: " + e.getMessage());
            result.setData(null);
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseObject<PhoneDTO>> login(@RequestBody PhoneDTO phoneDTO) {
        ResponseObject<PhoneDTO> result = new ResponseObject<>();
        String otp = userService.generateOtp(phoneDTO.getPhoneNumber());
        phoneDTO.setOtp(otp);
        result.setMessage("OTP sent successfully");
        result.setData(phoneDTO);
        twilioService.sendOtp(phoneDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/{userId}/fcm-token")
    public ResponseEntity<String> updateFcmToken(@PathVariable Long userId, @RequestBody String fcmToken) {
        userService.updateFcmToken(userId, fcmToken);
        return ResponseEntity.ok("FCM token updated successfully.");
    }

   
}
