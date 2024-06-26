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
import jpa.spring.core.ResponseObject;
import jpa.spring.model.dto.ChangPasswordDTO;
import jpa.spring.model.entities.User;
import jpa.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequiredArgsConstructor
@RequestMapping("/funtionUser")
public class FuntionUser {

    @Autowired
    private final UserService userService;

    @PutMapping("/changpassword")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject<User>> authenticate(@RequestBody ChangPasswordDTO changPasswordDTO) {
        ResponseObject<User> result = new ResponseObject<>();
        try {
            User user = userService.changPassword(changPasswordDTO);
            result.setMessage("Chang password successfully");
            result.setData(user);
            return new ResponseEntity<ResponseObject<User>>(result, HttpStatus.OK);
        } catch (Exception e) {
            result.setMessage("Chang password faild");
            result.setData(null);
            return new ResponseEntity<ResponseObject<User>>(result, HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/editUser")
    public String putMethodName(@PathVariable String id, @RequestBody String entity) {
        
        return entity;
    }

    @GetMapping("/logout")
    public ResponseEntity<ResponseObject<Void>> logout(HttpServletRequest request, HttpServletResponse response) {
        ResponseObject<Void> result = new ResponseObject<>();
        try {
            userService.logout(request, response);
            result.setMessage("Logout successfully");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            result.setMessage("Logout failed");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }
    

}
