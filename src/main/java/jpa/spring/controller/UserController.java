package jpa.spring.controller;

import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jpa.spring.core.ReponseObject;
import jpa.spring.model.User;
import jpa.spring.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    @Autowired
    private final UserService userService;

    @GetMapping("/getAll")
    public ResponseEntity<ReponseObject<List<User>>> getAllUser() {
        ReponseObject<List<User>> reponseObject = userService.getAllUser();
        if (reponseObject.getError() == null) {
            return ResponseEntity.ok(reponseObject);
        } else {
            return ResponseEntity.status(404).body(reponseObject);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ReponseObject<User>> registerUser(@RequestBody User newUser) {
        return userService.RegisterUser(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<ReponseObject<User>> loginUser(@RequestBody User loginUser, HttpSession httpSession, HttpServletResponse response, HttpServletRequest request ) {
        ReponseObject<User> responseObject = new ReponseObject<>();

        boolean isValidUser = userService.validateUserCredentials(loginUser);

        if (isValidUser) {
            httpSession = request.getSession();
            httpSession.setAttribute("username", new User());
            // httpSession.removeAttribute("username");
            // httpSession.invalidate();
            Cookie cookie = new Cookie("username", loginUser.getUsername());
            cookie.setMaxAge(7 * 24 *60 *60);
            cookie.setPath("/");
            responseObject.setMessage("success");
            responseObject.setData(loginUser);
            responseObject.setMessage("Login successful");
            response.addCookie(cookie);
            return ResponseEntity.ok(responseObject);
        } else {
            responseObject.setMessage("fail");
            responseObject.setMessage("Invalid username or password");
            return ResponseEntity.status(401).body(responseObject);
        }
    }
}
