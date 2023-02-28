package com.ritrovo.userservice.filter;

import com.ritrovo.userservice.model.JwtAuthenticationToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isNotBlank(authHeader) && authHeader.startsWith("Bearer ")) {
            String jwtToken = authHeader.substring(7);

            JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken();
            authenticationToken.setDetails(jwtToken);

            if (StringUtils.isNotBlank(jwtToken)) {
                Authentication authenticationResult = authenticationManager.authenticate(authenticationToken);
                if (authenticationResult.isAuthenticated()) {
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    context.setAuthentication(authenticationResult);
                    SecurityContextHolder.setContext(context);                }
            }

        }
        filterChain.doFilter(request, response);
    }
}
