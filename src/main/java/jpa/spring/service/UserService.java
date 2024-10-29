package jpa.spring.service;

import java.time.ZonedDateTime;
import java.util.*;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.servlet.http.*;
import jakarta.validation.Valid;
import jpa.spring.config.exception.*;
import jpa.spring.config.security.*;
import jpa.spring.core.ResponseObject;
import jpa.spring.model.dto.*;
import jpa.spring.model.entities.TokenAccount;
import jpa.spring.model.entities.User;
import jpa.spring.repository.*;
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

    // @Autowired
    // private final BaseRedisService baseRedisService;

    private final NotificationService notificationService;

    private final Map<String, String> otpStorage = new HashMap<>();

    private final Map<String, String> otpMail = new HashMap<>();

    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public List<User> getAllUser() {
        ResponseObject<List<User>> responseObject = new ResponseObject<>();
        List<User> users = repository.findAll();
        responseObject.setMessage("Success");
        responseObject.setData(users);
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

    public boolean validateUserCredentials(@RequestBody User loginUser) {
        System.out
                .println("Validating user: " + loginUser.getUsername() + " with password: " + loginUser.getPassword());
        return loginUser.getUsername().equals(loginUser.getUsername())
                && loginUser.getPassword().equals(loginUser.getPassword());
    }


    public UserCertification authetioncate(@Valid SigninDTO2 userSignin) {
    User user = repository.findByUsername(userSignin.getUsername())
                          .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(userSignin.getUsername(), userSignin.getPassword())
    );

    UserAccountDetail accountDetail = new UserAccountDetail(user);

    Optional<TokenAccount> existingTokenOpt = repositoryAccount.findByUsername(user.getUsername());

    TokenAccount tokenAccount = existingTokenOpt.orElse(new TokenAccount());
    tokenAccount.setAccsessToken(jwtTokenProvider.generateToken(accountDetail)); // Tạo Access Token mới
    tokenAccount.setRefreshToken(jwtTokenProvider.generateRefreshToken(accountDetail)); // Tạo Refresh Token mới
    tokenAccount.setUsername(user.getUsername());
    tokenAccount.setOwner(user);

    repositoryAccount.save(tokenAccount);

    notificationService.createAndSendNotification(user, "Login successfully");

    UserCertification certification = new UserCertification();
    certification.setUsername(user.getUsername());
    certification.setAccessToken(tokenAccount.getAccsessToken());
    certification.setExpiredTime(ZonedDateTime.now().plusMinutes(60));
    certification.setRefreshToken(tokenAccount.getRefreshToken());
    certification.setRefreshTime(ZonedDateTime.now().plusDays(7));

    return certification;
}


    
    public User changPassword(@Valid ChangPasswordDTO changPasswordDTO) {
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
            UserOTPVerificationDTO newOTPVerification = new UserOTPVerificationDTO();
            newOTPVerification.setUserId(username);
            newOTPVerification.setCreatedAt(ZonedDateTime.now());
            newOTPVerification.setExpiresAt(ZonedDateTime.now().plusMinutes(3));
            otpRepository.save(newOTPVerification);

            otpMail.put(email, otpString);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            // mailMessage.setFrom(mailUsername);
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

    public Boolean verifyOtp(String emal, String otp) {
        return otp.equals(otpMail.get(emal));
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
