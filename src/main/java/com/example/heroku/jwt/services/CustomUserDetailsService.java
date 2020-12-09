package com.example.heroku.jwt.services;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;

import static java.util.stream.Collectors.toList;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    //private UserRepository users;

    /*
    public CustomUserDetailsService(UserRepository users) {
        this.users = users;
    }
    */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return Arrays.asList("ROLE_ADMIN").stream().map(SimpleGrantedAuthority::new).collect(toList());
            }

            @Override
            public String getPassword() {
                return "{bcrypt}$2a$10$G/eP1R9nHzehxcgZdtX6OuVtKTiZg14W9eRd13VNCXChNSkUBPS.u";
            }

            @Override
            public String getUsername() {
                return "name";
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };
        /*
        return this.users.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username: " + username + " not found"));
        */
    }
}
