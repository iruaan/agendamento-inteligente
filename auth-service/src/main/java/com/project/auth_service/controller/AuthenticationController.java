package com.project.auth_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.project.auth_service.dto.AuthenticationResponse;
import com.project.auth_service.dto.LoginRequest;
import com.project.auth_service.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public String loginPage() {
        return authenticationService.loginPage();
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<AuthenticationResponse> loginRequest(@RequestBody LoginRequest loginRequest) {
        AuthenticationResponse response = authenticationService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

}
