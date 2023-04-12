package com.justeat.interview.employeeservice.resources;

import com.justeat.interview.employeeservice.domain.model.AuthenticationDto;
import com.justeat.interview.employeeservice.domain.model.RegisterDto;
import com.justeat.interview.employeeservice.response.AuthenticationResponse;
import com.justeat.interview.employeeservice.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final AuthenticationService service;

    @Operation(summary = "Register by providing details")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {

        log.info("Register request is received: {}", registerDto.getEmail());
        AuthenticationResponse authenticationResponse = service.register(registerDto);

        if (authenticationResponse == null) {
            log.error("Register failed for: {}", registerDto.getEmail());
            return ResponseEntity.badRequest().build();
        }

        log.info("Register successful for: {}", registerDto.getEmail());
        return ResponseEntity.ok(authenticationResponse);
    }

    @Operation(summary = "Authenticate with created email and password")
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationDto authenticationDto) {

        log.info("Login request is received: {}", authenticationDto.getEmail());
        return ResponseEntity.ok(service.authenticate(authenticationDto));
    }
}
