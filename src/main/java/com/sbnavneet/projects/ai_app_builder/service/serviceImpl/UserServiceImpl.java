package com.sbnavneet.projects.ai_app_builder.service.serviceImpl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sbnavneet.projects.ai_app_builder.dto.auth.UserProfileResponse;
import com.sbnavneet.projects.ai_app_builder.error.ResourceNotFoundException;
import com.sbnavneet.projects.ai_app_builder.repository.UserRepository;
import com.sbnavneet.projects.ai_app_builder.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserDetailsService{


    private final UserRepository userRepository;

    @Override
    public UserProfileResponse getProfile(Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProfile'");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", username));
    }

}
