package jpa.spring.controller;

import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jpa.spring.core.ReponseObject;
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
//import org.springframework.session.web.http.CookieSerializer;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private final UserService userService;

    // @Autowired
    // private final CookieSerializer cookieSerializer;

    @GetMapping("/getAll")
    public ResponseEntity<ReponseObject<List<User>>> getAllUser() {
        ReponseObject<List<User>> reponseObject = userService.getAllUser();
        if (reponseObject.getError() == null) {
            return ResponseEntity.ok(reponseObject);
        } else {
            return ResponseEntity.status(404).body(reponseObject);
        }
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<ReponseObject<User>> register(@RequestBody @Valid User register) {
        ReponseObject<User> result = new ReponseObject<>();
        try {
            User user = userService.register(register);
            result.setMessage("Create a new account successfully");
            result.setData(user);
            return new ResponseEntity<ReponseObject<User>>(result, HttpStatus.OK);
        } catch (Exception e) {
            result.setMessage("Failed to create a new account: " + e.getMessage());
            result.setData(null);
            return new ResponseEntity<ReponseObject<User>>(result, HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * @param user
     * @param httpSession
     * @param response
     * @param request
     * @return
     */
    @PostMapping("/signin")
    public ResponseEntity<ReponseObject<UserCertification>> authenticate(@RequestBody @Valid User user,
            HttpSession httpSession, HttpServletResponse response, HttpServletRequest request) {
        ReponseObject<UserCertification> result = new ReponseObject<>();
        try {
            UserCertification auth = userService.authetioncate(user);
            result.setMessage("sign in successfully");
            result.setData(auth);
            httpSession = request.getSession();
            httpSession.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            Cookie cookie = new Cookie("auth", auth.getAccessToken());
            cookie.setMaxAge(7 * 24 * 60 * 60); 
            cookie.setPath("/");
            response.addCookie(cookie);

            return new ResponseEntity<ReponseObject<UserCertification>>(result, HttpStatus.OK);
        } catch (Exception e) {
            result.setMessage("sign in Failed");
            result.setData(null);
            return new ResponseEntity<ReponseObject<UserCertification>>(result, HttpStatus.BAD_REQUEST);
        }
    }

}
