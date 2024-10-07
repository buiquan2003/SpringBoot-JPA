package jpa.spring.service;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.ServletException;
import jpa.spring.config.security.JwtTokenProvider;
import jpa.spring.config.security.UserAccountDetail;
import jpa.spring.model.dto.SignInDTO;
import jpa.spring.model.entities.Role;
import jpa.spring.model.entities.TokenAccount;
import jpa.spring.model.entities.User;
import jpa.spring.repository.RoleRepository;
import jpa.spring.repository.TokenAccountRepository;
import jpa.spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuth2AuthenticationService {

    // @PreAuthorize("hasAnyRole('USER', 'ADMIN')")

    private final UserRepository userRepository;
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenAccountRepository tokenRepository;
    private final RoleRepository roleRepository;

    @SuppressWarnings("unchecked")
    @Transactional
    public SignInDTO handleOAuth2Authentication(Authentication authentication) throws ServletException {
        OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauth2User = oauth2Token.getPrincipal();
        OAuth2AuthorizedClient authorizedClient = authorizedClientService
                .loadAuthorizedClient(oauth2Token.getAuthorizedClientRegistrationId(), oauth2Token.getName());

        String provider = authorizedClient.getClientRegistration().getRegistrationId();
        // String accessToken = authorizedClient.getAccessToken().getTokenValue();
        // String refreshToken = authorizedClient.getRefreshToken() != null
        // ? authorizedClient.getRefreshToken().getTokenValue()
        // : null;
      

        String username = oauth2User.getAttribute("name");
        String email = oauth2User.getAttribute("email");
        String gender = oauth2User.getAttribute("gender");
        String birthday = oauth2User.getAttribute("birthday");

        Object pictureObj = oauth2User.getAttribute("picture");
        String image = null;
        if (pictureObj instanceof String) {
            image = (String) pictureObj;
        } else if (pictureObj instanceof LinkedHashMap) {
            LinkedHashMap<String, Object> pictureMap = (LinkedHashMap<String, Object>) pictureObj;
            image = (String) pictureMap.get("url");
        }
        Optional<User> existingUser = userRepository.findByEmail(email);

        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole == null) {
            userRole = new Role();
            userRole.setName("ROLE_ADMIN");
            roleRepository.save(userRole);
        }
          User user = new User();


        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            user.setUsername(username);
            user.setEmail(email);
            user.setGender(gender);
            user.setBirthday(birthday);
            user.setRoles(Set.of(userRole));
            user.setImage(image);
            user = userRepository.save(user);
        }
  
        UserAccountDetail userDetails = new UserAccountDetail(user);
        String access_token = jwtTokenProvider.generateToken(userDetails);
        String refresh_token = jwtTokenProvider.generateRefreshToken(userDetails);
        Optional<TokenAccount> existingToken = tokenRepository.findByProvider(provider);
        TokenAccount token = existingToken.orElse(new TokenAccount());
        token.setOwner(user);
        token.setUsername(username);
        token.setAccsessToken(access_token);
        token.setRefreshToken(refresh_token);
        if (!existingToken.isPresent()) {
            token.setProvider(provider);
        }
        tokenRepository.save(token);

        return new SignInDTO(user.getUsername(), user.getEmail(), user.getImage(), access_token, refresh_token);
    }
}