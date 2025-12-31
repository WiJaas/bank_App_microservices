package com.ebank.authService.service;

import com.ebank.authService.dto.*;
import com.ebank.authService.model.Role;
import com.ebank.authService.model.User;
import com.ebank.authService.repository.RoleRepository;
import com.ebank.authService.repository.UserRepository;
import com.ebank.authService.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    public LoginResponse login(LoginRequest request) {

        // 1. Authenticate credentials
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2. Extract the authenticated principal (SECURITY DTO)
        UserDetailsDto principal =
                (UserDetailsDto) authentication.getPrincipal();

        // 3. Generate JWT (should include id + roles)
        String token = tokenProvider.generateToken(authentication);

        // 4. Build response
        return new LoginResponse(
                token,
                principal.getId(),
                principal.getUsername()
        );
    }

    @Transactional
    public CreateUserResponse registerClient(CreateUserRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        Role clientRole = roleRepository.findByName("CLIENT")
                .orElseThrow(() -> new RuntimeException("CLIENT role not found"));

        String tempPassword = generateTempPassword();

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(tempPassword))
                .roles(Set.of(clientRole))
                .build();

        userRepository.save(user);

        // TEMP (later: email service)
        System.out.println(
                "[AUTH] Temporary password for " + request.getEmail() + " = " + tempPassword
        );

        return new CreateUserResponse(user.getId());
    }

    private String generateTempPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }



}
