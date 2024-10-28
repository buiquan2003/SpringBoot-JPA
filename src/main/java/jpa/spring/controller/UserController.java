package jpa.spring.controller;

import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jpa.spring.config.utils.CookeiUtils;
import jpa.spring.config.utils.SessionUtil;
import jpa.spring.core.ErrorObject;
import jpa.spring.core.ResponseObject;
import jpa.spring.model.dto.SigninDTO2;
import jpa.spring.model.dto.UserCertification;
import jpa.spring.model.entities.User;
import jpa.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private final UserService userService;

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole( 'ADMIN')")
    public ResponseEntity<ResponseObject<User>> getAllUser() {

        ResponseObject<User> result = new ResponseObject<>();
        try {
            List<User> user = userService.getAllUser();
            result.setDaList(user);
            result.setMessage("Get profile successfully");
            return new ResponseEntity<ResponseObject<User>>(result, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<ResponseObject<UserCertification>> authenticate(
            @RequestBody @Valid SigninDTO2 signinDTO,
            HttpSession httpSession,
            HttpServletResponse response,
            HttpServletRequest request) {

        ResponseObject<UserCertification> result = new ResponseObject<>();

        try {
            UserCertification auth = userService.authetioncate(signinDTO);
            result.setMessage("Sign in successfully");
            result.setData(auth);
            SessionUtil.setAttribute(request, "SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            String sessionId = httpSession.getId();
            CookeiUtils.create(response, "JSESSIONID", sessionId, 7 * 24 * 60 * 60, true, true);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            ErrorObject error = new ErrorObject(ErrorObject.ErrorCode.DATA_INVALID, "Invalid username or password");
            result.setError(error);
            result.setMessage("Sign in failed");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ErrorObject error = new ErrorObject(ErrorObject.ErrorCode.ACCOUNT_EXIST, "An unknown error occurred");
            result.setError(error);
            result.setMessage("Sign in failed");

            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    // @PostMapping("/login/phone")
    // public ResponseEntity<ResponseObject<PhoneDTO>> login(@RequestBody PhoneDTO
    // phoneDTO) {
    // ResponseObject<PhoneDTO> result = new ResponseObject<>();
    // String otp = userService.generateOtp(phoneDTO.getPhoneNumber());
    // phoneDTO.setOtp(otp);
    // result.setMessage("OTP sent successfully");
    // result.setData(phoneDTO);
    // twilioService.sendOtp(phoneDTO);
    // return new ResponseEntity<>(result, HttpStatus.OK);
    // }

    @PostMapping("/{userId}/fcm-token")
    public ResponseEntity<String> updateFcmToken(@PathVariable Long userId, @RequestBody String fcmToken) {
        userService.updateFcmToken(userId, fcmToken);
        return ResponseEntity.ok("FCM token updated successfully.");
    }

}
