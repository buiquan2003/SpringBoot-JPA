package jpa.spring.service;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jpa.spring.config.security.JwtTokenProvider;
import jpa.spring.config.security.UserAccountDetail;
import jpa.spring.core.ResponseObject;
import jpa.spring.model.dto.ChangPasswordDTO;
import jpa.spring.model.dto.UserCertification;
import jpa.spring.model.entities.TokenAccount;
import jpa.spring.model.entities.User;
import jpa.spring.repository.TokenAccountRepository;
import jpa.spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

    @Autowired
    private final UserRepository repository;

    @Autowired
    private final TokenAccountRepository repositoryAccount;

    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public List<User> getAllUser() {
        ResponseObject<List<User>> reponseObject = new ResponseObject<>();
        List<User> users = repository.findAll();
        reponseObject.setMessage("Success");
        reponseObject.setData(users);
        return users;
    }

    public User register(User registerDTO) {
        if (repository.findById(registerDTO.getUsername()).isPresent()) {
            throw new UnknownError("Username " + registerDTO.getUsername() + " is already exist.");
        }

        String hashPassword = bCryptPasswordEncoder.encode(registerDTO.getPassword());
        User newUserAccount = new User();
        newUserAccount.setUsername(registerDTO.getUsername());
        newUserAccount.setEmail(registerDTO.getEmail());
        newUserAccount.setPassword(hashPassword);
        newUserAccount.setRole("ROLE_USER");
        newUserAccount.setDelFlag(false);
        newUserAccount.setUTimestmap(ZonedDateTime.now());
        return repository.save(newUserAccount);
    }

    public boolean validateUserCredentials(@RequestBody User loginUser) {
        System.out
                .println("Validating user: " + loginUser.getUsername() + " with password: " + loginUser.getPassword());
        return loginUser.getUsername().equals(loginUser.getUsername())
                && loginUser.getPassword().equals(loginUser.getPassword());
    }

    public UserCertification authetioncate(User user) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        UserAccountDetail accountDetail = repository
                .findById(user.getUsername())
                .map(userAccount -> new UserAccountDetail(userAccount))
                .orElseThrow(() -> new UnknownError("User not found"));

        TokenAccount tokenAccount = new TokenAccount();
        tokenAccount.setAccsessToken(jwtTokenProvider.generateToken(accountDetail));
        tokenAccount.setRefreshToken(jwtTokenProvider.generateRefreshToken(accountDetail));
        tokenAccount.setUsername(user.getUsername());
        tokenAccount.setOwner(user);
        repositoryAccount.save(tokenAccount);

        UserCertification certification = new UserCertification();
        certification.setUsername(user.getUsername());
        certification.setAccessToken(tokenAccount.getAccsessToken());
        certification.setExpiredTime(ZonedDateTime.now());
        certification.setRefreshToken(tokenAccount.getRefreshToken());
        certification.setRefreshTime(ZonedDateTime.now());
        return certification;

    }

    public User changPassword(ChangPasswordDTO changPasswordDTO) {
        String currentUsername = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUsername();
        User user = repository.findByUsername(currentUsername)
                .orElseThrow(() -> new UnknownError("User not found"));
        String hashPassword = bCryptPasswordEncoder.encode(changPasswordDTO.getNewPassword());
        user.setPassword(hashPassword);
        repository.save(user);
        return user;
    }

    public User editUser(User user) {
        String currentUsername = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUsername();
        User edit = repository.findByUsername(currentUsername).orElseThrow(() -> new UnknownError(" user not found"));
        String eamil = user.getEmail();
        edit.setEmail(eamil);
        repository.save(edit);
        return edit;
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
    }
}
