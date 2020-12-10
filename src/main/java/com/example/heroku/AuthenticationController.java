package com.example.heroku;


import com.example.heroku.jwt.JwtTokenProvider;
import com.example.heroku.request.data.AuthenticationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Value("${authentication.auth.accessTokenCookieName}")
    private String accessTokenCookieName;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;
/*
    @Autowired
    UserRepository users;
*/
    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody AuthenticationRequest data) {

        try {
            String username = data.getUsername();
            System.out.println(username);
            System.out.println(data.getPassword());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            String token = jwtTokenProvider.createToken(username, Arrays.asList("ROLE_ADMIN", "ROLE_USER"));

            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);
            ResponseCookie cookie = ResponseCookie.from(accessTokenCookieName, token)
                    .httpOnly(true)
                    .secure(false)///// must true
                    .maxAge(3600)
                    .build();

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString());
            return ok().headers(responseHeaders).body(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

    @GetMapping("/me")
    public ResponseEntity currentUser(@AuthenticationPrincipal UserDetails userDetails, @RequestBody AuthenticationRequest data){
        System.out.println(data.getPassword());
        Map<Object, Object> model = new HashMap<>();
        model.put("username", userDetails.getUsername());
        model.put("roles", userDetails.getAuthorities()
                .stream()
                .map(a -> ((GrantedAuthority) a).getAuthority())
                .collect(toList())
        );
        return ok(model);
    }
}
