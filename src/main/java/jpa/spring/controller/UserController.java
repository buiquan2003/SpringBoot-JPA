package jpa.spring.controller;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jpa.spring.core.ReponseObject;
// import jpa.spring.model.dto.ChangPasswordDTO;
import jpa.spring.model.dto.UserCertification;
import jpa.spring.model.entities.User;
import jpa.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
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

    @PostMapping(path = "/signup")
    public ResponseEntity<ReponseObject<User>> register(@RequestBody @Valid User register) throws Exception {
        User user = userService.register(register);
        ReponseObject<User> result = new ReponseObject<>();
        result.setMessage("Create a new account successfully");
        result.setData(user);
        return new ResponseEntity<ReponseObject<User>>(result, HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<ReponseObject<UserCertification>> authenticate(@RequestBody User user) {
        UserCertification auth = userService.authetioncate(user);
        ReponseObject<UserCertification> result = new ReponseObject<>();
        result.setMessage("sign in successfully");
        result.setData(auth);
        return new ResponseEntity<ReponseObject<UserCertification>>(result, HttpStatus.OK);
    }

    // @PutMapping("/changPassword")
    // public ResponseEntity<ReponseObject<User>> authenticate(@RequestBody ChangPasswordDTO changPasswordDTO)
    //         throws Exception {
    //     User user = userService.changPassword(changPasswordDTO);
    //     ReponseObject<User> result = new ReponseObject<>();
    //     result.setMessage("Chang password successfully");
    //     result.setData(user);
    //     return new ResponseEntity<ReponseObject<User>>(result, HttpStatus.OK);
    // }

}
