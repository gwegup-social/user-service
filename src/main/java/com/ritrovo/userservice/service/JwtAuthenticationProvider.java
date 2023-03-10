package com.ritrovo.userservice.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ritrovo.userservice.model.JwtAuthenticationToken;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final AuthService authService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationProvider(AuthService authService,
                                     @Qualifier("jwtUserDetailsService") UserDetailsService userDetailsService) {
        this.authService = authService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if (Objects.nonNull(authentication)) {
            Object jwtTokenObj = authentication.getDetails();
            if (Objects.nonNull(jwtTokenObj)) {
                String jwtToken = jwtTokenObj.toString();
                boolean isTokenValid = authService.validateJwtToken(jwtToken);
                if (isTokenValid) {
                    DecodedJWT decodedToken = JWT.decode(jwtToken);
                    Map<String, Claim> claims = decodedToken.getClaims();
                    if (MapUtils.isNotEmpty(claims)) {
                        Claim subjectClaim = claims.get("sub");
                        if (Objects.nonNull(subjectClaim)) {
                            String userId = subjectClaim.asString();

                            if (StringUtils.isNotBlank(userId)) {
                                UserDetails loadedUserDetails = userDetailsService.loadUserByUsername(userId);
                                JwtAuthenticationToken authenticatedToken = new JwtAuthenticationToken(userId, null);
                                authenticatedToken.setAuthenticated(true);
                                Set<String> authorities = loadedUserDetails
                                        .getAuthorities()
                                        .stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .collect(Collectors.toSet());
                                authenticatedToken.setRoles(authorities);
                                return authenticatedToken;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
