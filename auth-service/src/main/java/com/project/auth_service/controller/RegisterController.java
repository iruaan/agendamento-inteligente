package com.project.auth_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.project.auth_service.dto.AuthenticationResponse;
import com.project.auth_service.dto.RegisterRequest;
import com.project.auth_service.service.RegisterService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;

    @GetMapping("/register")
    public String page() {
        return registerService.RegisterPage();
    }

    @PostMapping("/api/auth/register")
    public ResponseEntity<AuthenticationResponse> Register(@RequestBody RegisterRequest registerRequest){

    AuthenticationResponse response = registerService.register(registerRequest);
    return ResponseEntity.ok(response);




    }

}
