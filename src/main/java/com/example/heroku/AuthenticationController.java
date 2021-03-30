package com.example.heroku;


import com.example.heroku.jwt.JwtTokenProvider;
import com.example.heroku.request.data.AuthenticationRequest;
import com.example.heroku.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    @Value("${authentication.auth.accessTokenCookieName}")
    private String accessTokenCookieName;

    @Value("${authentication.auth.httponlysecure}")
    private boolean httponlysecure;

    private final ReactiveAuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signin")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity> signin(@Valid @RequestBody Mono<AuthenticationRequest> authRequest) {

        try {
            return authRequest
                    .flatMap(login -> this.authenticationManager
                            .authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()))
                            .map(this.jwtTokenProvider::createToken)
                    )
                    .map(jwt -> {
                        HttpHeaders httpHeaders = new HttpHeaders();
                        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
                        Map<Object, Object> model = new HashMap<>();
                        model.put("token", jwt);
                        ResponseCookie cookie = ResponseCookie.from(accessTokenCookieName, jwt)
                                .path("/")
                                .httpOnly(true)
                                .secure(httponlysecure)///// must true
                                .maxAge(3600)
                                .build();
                        httpHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString());
                        return new ResponseEntity<>(model, httpHeaders, HttpStatus.OK);
                    });
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

    @GetMapping("/me")
    public Mono<Map<String, Object>> current(@AuthenticationPrincipal Mono<UserDetails> principal) {
        return principal.map(user -> new HashMap<String, Object>() {{
                    put("name", user.getUsername());
                    put("roles", AuthorityUtils.authorityListToSet(user.getAuthorities()));
                }}
        );
    }
}
