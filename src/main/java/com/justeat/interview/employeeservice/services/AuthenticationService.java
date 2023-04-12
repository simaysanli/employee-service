package com.justeat.interview.employeeservice.services;

import com.justeat.interview.employeeservice.domain.model.AuthenticationDto;
import com.justeat.interview.employeeservice.domain.model.RegisterDto;
import com.justeat.interview.employeeservice.entity.Role;
import com.justeat.interview.employeeservice.entity.Token;
import com.justeat.interview.employeeservice.entity.TokenType;
import com.justeat.interview.employeeservice.entity.User;
import com.justeat.interview.employeeservice.repository.TokenRepository;
import com.justeat.interview.employeeservice.repository.UserRepository;
import com.justeat.interview.employeeservice.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterDto registerDto) {
        boolean mailExists = userRepository.existsByEmail(registerDto.getEmail());

        if (mailExists) {
            log.error("Mail could not found in database for: {}", registerDto.getEmail());
            return null;
        }

        User user = User.builder()
                .firstName(registerDto.getFirstName())
                .lastName(registerDto.getLastName())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .role(Role.USER)
                .additionalInfo(registerDto.getAdditionalInfo())
                .build();

        log.info("Saving user to the database.");
        User savedUser = this.userRepository.save(user);

        log.info("Generating token.");
        String jwtToken = jwtService.generateToken(user);

        log.info("Saving token to the database.");
        saveUserToken(savedUser, jwtToken);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationDto authenticationDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationDto.getEmail(),
                        authenticationDto.getPassword()));

        log.info("Trying to find user with email.");
        User user = this.userRepository.findByEmail(authenticationDto.getEmail())
                .orElseThrow();

        log.info("Generating token.");
        String jwtToken = jwtService.generateToken(user);

        log.info("Revoking other tokens.");
        revokeAllUserTokens(user);

        log.info("Saving tokens to the database.");
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
