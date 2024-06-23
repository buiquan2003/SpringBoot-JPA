package jpa.spring.controller;

import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jpa.spring.config.utils.CookeiUtils;
import jpa.spring.config.utils.SessionUtil;
import jpa.spring.core.ResponseObject;
import jpa.spring.model.dto.UserCertification;
import jpa.spring.model.entities.User;
import jpa.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    // @Autowired
    // private final CookieSerializer cookieSerializer;

    @GetMapping("/getAll")
    public ResponseEntity<ResponseObject<List<User>>> getAllUser() {
        ResponseObject<List<User>> reponseObject = userService.getAllUser();
        if (reponseObject.getError() == null) {
            return ResponseEntity.ok(reponseObject);
        } else {
            return ResponseEntity.status(404).body(reponseObject);
        }
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<ResponseObject<User>> register(@RequestBody @Valid User register) {
        ResponseObject<User> result = new ResponseObject<>();
        try {
            User user = userService.register(register);
            result.setMessage("Create a new account successfully");
            result.setData(user);
            return new ResponseEntity<ResponseObject<User>>(result, HttpStatus.OK);
        } catch (Exception e) {
            result.setMessage("Failed to create a new account: " + e.getMessage());
            result.setData(null);
            return new ResponseEntity<ResponseObject<User>>(result, HttpStatus.BAD_REQUEST);
        }

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

            return new ResponseEntity<ResponseObject<UserCertification>>(result, HttpStatus.OK);
        } catch (Exception e) {
            result.setMessage("sign in Failed");
            result.setData(null);
            return new ResponseEntity<ResponseObject<UserCertification>>(result, HttpStatus.BAD_REQUEST);
        }
    }

}
