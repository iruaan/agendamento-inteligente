package com.project.auth_service.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.project.auth_service.dto.AuthenticationResponse;
import com.project.auth_service.dto.RegisterRequest;
import com.project.auth_service.model.User;
import com.project.auth_service.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Service
@Getter
@Setter
@AllArgsConstructor
public class RegisterService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String RegisterPage() {
        return "index.html";
    }

    public AuthenticationResponse register(@RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setName(registerRequest.getName());
        user.setSlug(generateSlug(registerRequest.getName()));

        userRepository.save(user);
        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(token);

    }

    private String generateSlug(String name) {
        return name.toLowerCase().replace(" ", "-");
    }
}
