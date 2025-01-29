package com.programming.controller;

import com.programming.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/api/generate-token")
    public ResponseEntity<String> generateToken(@RequestParam String username) {
        String generateToken = authService.generateToken(username);
        return new ResponseEntity<>(generateToken, HttpStatus.OK);
    }
}
