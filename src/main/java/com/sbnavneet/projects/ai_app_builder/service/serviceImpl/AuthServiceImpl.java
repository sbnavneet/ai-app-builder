package com.sbnavneet.projects.ai_app_builder.service.serviceImpl;

import org.springframework.stereotype.Service;

import com.sbnavneet.projects.ai_app_builder.dto.auth.AuthResponse;
import com.sbnavneet.projects.ai_app_builder.dto.auth.LoginRequest;
import com.sbnavneet.projects.ai_app_builder.dto.auth.SignupRequest;
import com.sbnavneet.projects.ai_app_builder.entity.User;
import com.sbnavneet.projects.ai_app_builder.mapper.UserMapper;
import com.sbnavneet.projects.ai_app_builder.repository.UserRepository;
import com.sbnavneet.projects.ai_app_builder.service.AuthService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public AuthResponse signup(SignupRequest signupRequest) {
        User user = User.builder().email(signupRequest.email())
                                  .passwordHash(signupRequest.password())
                                  .name(signupRequest.name())
                                  .build();
        user = userRepository.save(user);

        return new AuthResponse("ascbdxert", userMapper.toUserProfileResponse(user));
    }

    @Override
    public AuthResponse login(LoginRequest loginDto) {
        return null;
    }

}
