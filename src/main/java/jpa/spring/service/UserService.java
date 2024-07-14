package jpa.spring.service;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
import jpa.spring.config.exception.UnknowException;
import jpa.spring.config.exception.UserAccountNotFoundException;
import jpa.spring.config.security.JwtTokenProvider;
import jpa.spring.config.security.UserAccountDetail;
import jpa.spring.core.ResponseObject;
import jpa.spring.model.dto.ChangPasswordDTO;
import jpa.spring.model.dto.UserCertification;
import jpa.spring.model.dto.UserOTPVerificationDTO;
import jpa.spring.model.entities.TokenAccount;
import jpa.spring.model.entities.User;
import jpa.spring.repository.TokenAccountRepository;
import jpa.spring.repository.UserOTPRepository;
import jpa.spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

    @Autowired
    private final UserRepository repository;

    @Autowired
    private final TokenAccountRepository repositoryAccount;

    @Autowired
    private final UserOTPRepository otpRepository;

    @Autowired
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Value("${security.jwt.secret-key}")
    private String jwtAccessKey;

    private final Map<String, String> otpStorage = new HashMap<>();

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

    public UserCertification getUserByPhone(String phone) {
        Optional<User> userOpt = repository.findByPhone(phone);
        if (!userOpt.isPresent()) {
            throw new UnknowException("User with ID " + phone + " does not exist.");
        }
        return null;

    }
        

     public User getUserById(Long userId) {
        Optional<User> userOpt = repository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new UnknowException("User with ID " + userId + " does not exist.");
        }
        return userOpt.get();
    }

    public User register(User registerDTO) {
        if (repository.findByUsername(registerDTO.getUsername()).isPresent()) {
            throw new UnknownError("Username " + registerDTO.getUsername() + " is already exist.");
        }

        String hashPassword = bCryptPasswordEncoder.encode(registerDTO.getPassword());
        User newUserAccount = new User();
        newUserAccount.setUsername(registerDTO.getUsername());
        newUserAccount.setEmail(registerDTO.getEmail());
        newUserAccount.setPassword(hashPassword);
        newUserAccount.setRole("ROLE_USER");
        newUserAccount.setAddress(registerDTO.getAddress());
        newUserAccount.setPhone(registerDTO.getPhone());
        newUserAccount.setAuthType(registerDTO.getAuthType());
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
                .findByUsername(user.getUsername())
                .map(userAccount -> new UserAccountDetail(userAccount))
                .orElseThrow(() -> new UnknownError("User not found"));

        TokenAccount tokenAccount = new TokenAccount();
        tokenAccount.setAccsessToken(jwtTokenProvider.generateToken(accountDetail));
        tokenAccount.setRefreshToken(jwtTokenProvider.generateRefreshToken(accountDetail));
        tokenAccount.setUsername(user.getUsername());
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

    public Response sendOTPVerificationEmail(String username, String email) throws Exception {
        try {
            Random random = new Random();
            int otp = 100000 + random.nextInt(900000);
            String otpString = String.format("%06d", otp);

            String hashedOTP = bCryptPasswordEncoder.encode(otpString);

            UserOTPVerificationDTO newOTPVerification = new UserOTPVerificationDTO();
            newOTPVerification.setUserId(username);
            newOTPVerification.setOtp(hashedOTP);
            newOTPVerification.setCreatedAt(ZonedDateTime.now());
            newOTPVerification.setExpiresAt(ZonedDateTime.now().plusMinutes(3));
            otpRepository.save(newOTPVerification);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(mailUsername);
            mailMessage.setTo(email);
            mailMessage.setSubject("Smart Lunch OTP Verification Code");
            mailMessage.setText("Your OTP code is: " + otpString);

            mailSender.send(mailMessage);

            System.out.println("OTP Email sent to " + email);
            return new Response();

        } catch (Exception e) {
            System.out.println("Error in sendOTPVerificationEmail: " + e.getMessage());
            throw new Exception("Error occurred while sending OTP email", e);
        }
    }

    public void updateFcmToken(Long userId, String fcmToken) {
        Optional<User> userOptional = repository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setFcmToken(fcmToken);
            repository.save(user);
        } else {
            throw new UserAccountNotFoundException("User with ID " + userId + " not found.");
        }
    }

    public Map<String, String> signinWithGoogle(Map<String, String> body) {
        Map<String, String> tokens = new HashMap<>();
        String email = body.get("email");
        String googleId = body.get("googleId");

        User existingUser = repository.findByEmail(email).orElse(null);

        if (existingUser != null) {
            existingUser.setGoogleId(googleId);
            existingUser.setAuthType("google");
            repository.save(existingUser);

            String accessToken = jwtTokenProvider.generateAccessTokenPhone(existingUser);
            String refreshToken = jwtTokenProvider.generateRefreshTokenPhone(existingUser);
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);
        } else {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername(body.get("username"));
            newUser.setGoogleId(googleId);
            newUser.setAuthType("google");
            newUser.setPassword(bCryptPasswordEncoder.encode(googleId)); 
            newUser.setVerified(true);
            repository.save(newUser);
            existingUser = newUser;
        }

        String accessToken = jwtTokenProvider.generateAccessTokenPhone(existingUser);
        String refreshToken = jwtTokenProvider.generateRefreshTokenPhone(existingUser);
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }
        
    public String generateOtp(String phoneNumber) {
        Optional<User> user = repository.findByPhone(phoneNumber);

        if (!user.isPresent()) {
            User newUser = new User();
            newUser.setPhone(phoneNumber);
            newUser.setVerified(false); 
            newUser.setAuthType("phone");
            repository.save(newUser);
        }

        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStorage.put(phoneNumber, otp);
        return otp;
    }

    public boolean validateOtp(String phoneNumber, String otp) {
        return otp.equals(otpStorage.get(phoneNumber));
    }

    public Map<String, String> signInWithPhone(String phoneNumber, String otp) {
        if (validateOtp(phoneNumber, otp)) {
            User existingUser = repository.findByPhone(phoneNumber)
                    .orElseThrow(() -> new IllegalArgumentException("Phone number not found"));

            String accessToken = jwtTokenProvider.generateRefreshTokenPhone(existingUser);
            String refreshToken = jwtTokenProvider.generateRefreshTokenPhone(existingUser);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);
            return tokens;
        } else {
            throw new RuntimeException("Invalid OTP");
        }
    }

    public Map<String, String> registerAndSignInWithPhone(String phoneNumber, String otp) {
        if (validateOtp(phoneNumber, otp)) {
            Optional<User> userOpt = repository.findByPhone(phoneNumber);
            User user;

            if (userOpt.isPresent()) {
                user = userOpt.get();
            } else {
                user = new User();
                user.setPhone(phoneNumber);
                user.setVerified(true);
                repository.save(user);
            }

            String accessToken = jwtTokenProvider.generateRefreshTokenPhone(user);
            String refreshToken = jwtTokenProvider.generateRefreshTokenPhone(user);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);
            return tokens;
        } else {
            throw new RuntimeException("Invalid OTP");
        }
    }

}
