package com.ritrovo.userservice.service;

import com.google.common.collect.Lists;
import com.ritrovo.userservice.dao.AuthDetailsRepository;
import com.ritrovo.userservice.entity.AuthDetails;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MongoUserDetailsService implements UserDetailsService {

    private final AuthDetailsRepository authDetailsRepository;

    public MongoUserDetailsService(AuthDetailsRepository authDetailsRepository) {
        this.authDetailsRepository = authDetailsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<AuthDetails> userOptional = authDetailsRepository.findByUserId(username);

        AuthDetails authDetails = userOptional.orElseThrow(() -> new UsernameNotFoundException("could not find user"));

        Set<String> authorities = authDetails.getAuthorities();
        List<SimpleGrantedAuthority> grantedAuthorities = Lists.newArrayList();

        if (CollectionUtils.isNotEmpty(authorities)) {
            grantedAuthorities = authorities
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

        return new User(username,
                "dummy",
                grantedAuthorities
        );
    }
}
