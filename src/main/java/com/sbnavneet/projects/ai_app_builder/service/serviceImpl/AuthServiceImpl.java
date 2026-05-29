package com.sbnavneet.projects.ai_app_builder.service.serviceImpl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sbnavneet.projects.ai_app_builder.dto.auth.AuthResponse;
import com.sbnavneet.projects.ai_app_builder.dto.auth.LoginRequest;
import com.sbnavneet.projects.ai_app_builder.dto.auth.SignupRequest;
import com.sbnavneet.projects.ai_app_builder.entity.User;
import com.sbnavneet.projects.ai_app_builder.error.BadRequestException;
import com.sbnavneet.projects.ai_app_builder.mapper.UserMapper;
import com.sbnavneet.projects.ai_app_builder.repository.UserRepository;
import com.sbnavneet.projects.ai_app_builder.security.AuthUtility;
import com.sbnavneet.projects.ai_app_builder.service.AuthService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthUtility authUtility;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse signup(SignupRequest signupRequest) {
        userRepository.findByUsername(signupRequest.username()).ifPresent(user -> {
            throw new BadRequestException("User already exists with username" + signupRequest.username());
        });
        User user = userMapper.toEntity(signupRequest);
        user.setPassword(passwordEncoder.encode(signupRequest.password()));
        user = userRepository.save(user);
        String accessToken = authUtility.generateAccessToken(user);
        String refreshToken = authUtility.generateRefreshToken(user);
        return new AuthResponse(accessToken, userMapper.toUserProfileResponse(user), refreshToken);
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));
        User user = (User) authentication.getPrincipal();
        String accessToken = authUtility.generateAccessToken(user);
        String refreshToken = authUtility.generateRefreshToken(user);
        return new AuthResponse(accessToken, userMapper.toUserProfileResponse(user), refreshToken);
    }

    @Override
    public AuthResponse refresh(String refreshToken) {
        String username = authUtility.validateRefreshToken(refreshToken);
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new BadRequestException("User not found"));
        String newAccessToken = authUtility.generateAccessToken(user);
        String newRefreshToken = authUtility.generateRefreshToken(user);
        return new AuthResponse(newAccessToken, userMapper.toUserProfileResponse(user), newRefreshToken);
    }

}
