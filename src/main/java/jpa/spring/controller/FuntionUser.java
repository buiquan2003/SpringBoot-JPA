package jpa.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jpa.spring.core.ResponseObject;
import jpa.spring.model.dto.ChangPasswordDTO;
import jpa.spring.model.entities.User;
import jpa.spring.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FuntionUser {

    @Autowired
    private final UserService userService;

    @PutMapping("/user/funUser/changpassword")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ResponseObject<User>> authenticate(@Valid @RequestBody ChangPasswordDTO changPasswordDTO) {
        ResponseObject<User> result = new ResponseObject<>();
        User user = userService.changPassword(changPasswordDTO);
        result.setMessage("Chang password successfully");
        result.setData(user);
        return new ResponseEntity<ResponseObject<User>>(result, HttpStatus.OK);
    }

    @PutMapping("/user/funUser/edit")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ResponseObject<User>> putMethodName(@RequestBody User user) {
        ResponseObject<User> result = new ResponseObject<>();
        User newUser = userService.editUser(user);
        result.setMessage("Chang password successfully");
        result.setData(newUser);
        return new ResponseEntity<ResponseObject<User>>(result, HttpStatus.OK);
    }

    @GetMapping("/user/funUser/logout")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ResponseObject<Void>> logout(HttpServletRequest request, HttpServletResponse response) {
        ResponseObject<Void> result = new ResponseObject<>();
        userService.logout(request, response);
        result.setMessage("Logout successfully");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
