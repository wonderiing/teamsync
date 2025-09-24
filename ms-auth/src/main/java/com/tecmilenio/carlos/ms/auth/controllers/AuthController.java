package com.tecmilenio.carlos.ms.auth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tecmilenio.carlos.ms.auth.dto.LoginRequest;
import com.tecmilenio.carlos.ms.auth.dto.LoginResponse;
import com.tecmilenio.carlos.ms.auth.dto.UserInfo;
import com.tecmilenio.carlos.ms.auth.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            LoginResponse response = userService.authenticate(loginRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/login/email")
    public ResponseEntity<LoginResponse> loginWithEmail(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            LoginResponse response = userService.authenticateByEmail(loginRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/validate")
    public ResponseEntity<UserInfo> validateToken(@RequestHeader("Authorization") String token) {
        try {
            // Remove "Bearer " prefix if present
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            if (userService.validateToken(token)) {
                UserInfo userInfo = userService.getUserInfoFromToken(token);
                return ResponseEntity.ok(userInfo);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/login/hr")
    public ResponseEntity<LoginResponse> loginHr(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            LoginResponse response = userService.authenticateByEmail(loginRequest);
            // Verificar que el usuario tenga rol HR
            if (!"HR".equals(response.getRole())) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/login/admin")
    public ResponseEntity<LoginResponse> loginAdmin(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            LoginResponse response = userService.authenticateByEmail(loginRequest);
            // Verificar que el usuario tenga rol ADMIN
            if (!"ADMIN".equals(response.getRole())) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
