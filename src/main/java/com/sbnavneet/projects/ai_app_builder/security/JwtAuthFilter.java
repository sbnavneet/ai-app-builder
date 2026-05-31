package com.sbnavneet.projects.ai_app_builder.security;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final AuthUtility authUtility;
    private final HandlerExceptionResolver handlerExceptionResolver;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
            try{
                    final String requestHeaderToken = request.getHeader("Authorization");

                    if(requestHeaderToken == null || !requestHeaderToken.startsWith("Bearer")){
                        filterChain.doFilter(request, response);
                        return;
                    }
                
                    String jwtToken = requestHeaderToken.split("Bearer ")[1];
                    JwtUserPrincipal jwtUserPrincipal = authUtility.validateAccessToken(jwtToken);
                
                    if(jwtUserPrincipal != null && SecurityContextHolder.getContext().getAuthentication() == null){
                        UsernamePasswordAuthenticationToken authenticationFilter = new UsernamePasswordAuthenticationToken(
                            jwtUserPrincipal, null, jwtUserPrincipal.authorities()
                        );
                        SecurityContextHolder.getContext().setAuthentication(authenticationFilter);
                    }
                
                    filterChain.doFilter(request, response);
            }catch(Exception e){
                handlerExceptionResolver.resolveException(request, response, null, e);
            }


    }

}
