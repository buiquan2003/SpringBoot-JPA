package jpa.spring.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import jpa.spring.core.ReponseObject;
import jpa.spring.core.furiError;
import jpa.spring.model.User;
import jpa.spring.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public ReponseObject<List<User>> getAllUser() {
        ReponseObject<List<User>> reponseObject = new ReponseObject<>();
        List<User> users = repository.findAll();
        reponseObject.setMessage("Success");
        reponseObject.setData(users);
        return reponseObject;

    }

    public ResponseEntity<ReponseObject<User>> RegisterUser(@RequestBody User newUser) {
        List<User> foundUser = repository.findByUsername(newUser.getUsername().trim());
        furiError error = new furiError("404", "Tên người dùng đã tồn tại");
        if (!foundUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ReponseObject<User>(
                            "false",
                            Collections.singletonList(error),
                            null,
                            null));
        }
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String hashPassword = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(hashPassword);
        User savedUser = repository.save(newUser);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ReponseObject<User>(
                        "true",
                        null,
                        null,
                        savedUser));
    }

    public boolean validateUserCredentials(@RequestBody User loginUser) {
        System.out.println("Validating user: " + loginUser.getUsername() + " with password: " + loginUser.getPassword()); 
        return loginUser.getUsername().equals(loginUser.getUsername()) && loginUser.getPassword().equals(loginUser.getPassword());
    }
}
