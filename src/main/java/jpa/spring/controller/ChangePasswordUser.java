package jpa.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jpa.spring.core.ReponseObject;
import jpa.spring.model.dto.ChangPasswordDTO;
import jpa.spring.model.entities.User;
import jpa.spring.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/changePassword")
public class ChangePasswordUser {

     @Autowired
    private final UserService userService;
    
    @PutMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<ReponseObject<User>> authenticate(@RequestBody ChangPasswordDTO changPasswordDTO)
            throws Exception {
        User user = userService.changPassword(changPasswordDTO);
        ReponseObject<User> result = new ReponseObject<>();
        result.setMessage("Chang password successfully");
        result.setData(user);
        return new ResponseEntity<ReponseObject<User>>(result, HttpStatus.OK);
    }
}
