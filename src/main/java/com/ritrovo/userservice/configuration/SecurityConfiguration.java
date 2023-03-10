package com.ritrovo.userservice.configuration;

import com.ritrovo.userservice.service.JwtAuthenticationProvider;
import com.ritrovo.userservice.service.MongoAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final MongoAuthenticationProvider mongoAuthenticationProvider;
    private final JwtAuthConfigurer jwtAuthConfigurer;


    public SecurityConfiguration(JwtAuthenticationProvider jwtAuthenticationProvider,
                                 MongoAuthenticationProvider mongoAuthenticationProvider,
                                 JwtAuthConfigurer jwtAuthConfigurer) {
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
        this.mongoAuthenticationProvider = mongoAuthenticationProvider;
        this.jwtAuthConfigurer = jwtAuthConfigurer;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .httpBasic(Customizer.withDefaults())
                .apply(jwtAuthConfigurer)
                .and()
                .authorizeRequests()
                .antMatchers("/api/user/**")
                .authenticated()
                .antMatchers("/api/**")
                .permitAll()
                .and()
                .csrf()
                .disable()
                .anonymous()
                .disable()
                .authenticationProvider(jwtAuthenticationProvider)
                .authenticationProvider(mongoAuthenticationProvider);

        return http.build();
    }


}
