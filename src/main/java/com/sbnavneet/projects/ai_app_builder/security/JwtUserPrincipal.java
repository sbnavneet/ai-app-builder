package com.sbnavneet.projects.ai_app_builder.security;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

public record JwtUserPrincipal(Long userId, String username, List<GrantedAuthority> authorities) {

}
