package com.sbnavneet.projects.ai_app_builder.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sbnavneet.projects.ai_app_builder.dto.auth.AuthResponse;
import com.sbnavneet.projects.ai_app_builder.dto.auth.LoginRequest;
import com.sbnavneet.projects.ai_app_builder.dto.auth.SignupRequest;
import com.sbnavneet.projects.ai_app_builder.dto.auth.UserProfileResponse;
import com.sbnavneet.projects.ai_app_builder.service.AuthService;
import com.sbnavneet.projects.ai_app_builder.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService ;
    private final UserService userService ;
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody LoginRequest loginDto){
        return ResponseEntity.ok(authService.login(loginDto));
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest signupRequest){
        return ResponseEntity.ok(authService.signup(signupRequest));
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile(){
        Long userId = 1L ; //get user id from security contenxt TODO
        return ResponseEntity.ok(userService.getProfile(userId));
    }
}
