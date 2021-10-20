package com.example.heroku;


import com.example.heroku.jwt.JwtTokenProvider;
import com.example.heroku.model.Users;
import com.example.heroku.model.repository.UserRepository;
import com.example.heroku.request.data.AuthenticationRequest;
import com.example.heroku.request.data.UpdatePassword;
import com.example.heroku.response.Format;
import com.example.heroku.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    @Value("${authentication.auth.accessTokenCookieName}")
    private String accessTokenCookieName;

    @Value("${authentication.auth.httponlysecure}")
    private boolean httponlysecure;

    @Value("${authentication.auth.timeexpire}")
    private long validityInMs; // 1h

    private final ReactiveAuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    private ResponseEntity createAuthBearToken(String jwt) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
        Map<Object, Object> model = new HashMap<>();
        model.put("token", jwt);
        ResponseCookie cookie = ResponseCookie.from(accessTokenCookieName, jwt)
                .path("/")
                .httpOnly(httponlysecure)
                .secure(httponlysecure)///// must true
                .maxAge(validityInMs)
                .build();
        httpHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString());
        return new ResponseEntity<>(model, httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/signin")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity> signin(@Valid @RequestBody Mono<AuthenticationRequest> authRequest) {

        try {
            return authRequest
                    .flatMap(login -> this.authenticationManager
                            .authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()))
                            .map(this.jwtTokenProvider::createToken)
                    )
                    .map(this::createAuthBearToken);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

    @PostMapping("/refresh")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity> refreshToken(@AuthenticationPrincipal Mono<UserDetails> principal) {
        try {
            return principal
                    .map(this.jwtTokenProvider::createToken)
                    .map(this::createAuthBearToken);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Can not refresh Token");
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

    @PostMapping("/account/update")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity> update(@AuthenticationPrincipal Mono<UserDetails> principal, @Valid @RequestBody UpdatePassword update) {

        try {
            return principal
                    .flatMap(login -> this.authenticationManager
                            .authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), update.getOldpassword()))
                            .flatMap(authentication ->
                                    userRepository.updatePassword(
                                            login.getUsername(),
                                            this.passwordEncoder.encode(update.getNewpassword())
                                    )
                            )
                    )
                    .then(Mono.just(""))
                    .map(this::createAuthBearToken);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

    @PostMapping("/account/delete")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity> selfDelete(@AuthenticationPrincipal Mono<UserDetails> principal, @Valid @RequestBody UpdatePassword update) {

        try {
            return principal
                    .flatMap(login -> this.authenticationManager
                            .authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), update.getOldpassword()))
                            .filter(authentication ->
                                    authentication.getName().equals(update.getUsername()) &&
                                            getRole(login) != Util.ROLE.ROLE_ROOT
                            )
                            .switchIfEmpty(Mono.error(new AccessDeniedException("403 returned1")))
                            .flatMap(authentication ->
                                    userRepository.deleteByUserNameAndPassword(
                                            update.getUsername()
                                    )
                            )
                    )
                    .then(Mono.just(""))
                    .map(this::createAuthBearToken);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

    private Util.ROLE getRole(UserDetails principal) {
        if (principal.getAuthorities().size() > 1)
            return null;
        if (principal.getAuthorities().contains(new SimpleGrantedAuthority(Util.ROLE.ROLE_ROOT.getName())))
            return Util.ROLE.ROLE_ROOT;
        if (principal.getAuthorities().contains(new SimpleGrantedAuthority(Util.ROLE.ROLE_ADMIN.getName())))
            return Util.ROLE.ROLE_ADMIN;
        if (principal.getAuthorities().contains(new SimpleGrantedAuthority(Util.ROLE.ROLE_USER.getName())))
            return Util.ROLE.ROLE_USER;
        return null;
    }

    @PostMapping("/admin/account/delete")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity> delete(@AuthenticationPrincipal Mono<UserDetails> principal, @Valid @RequestBody UpdatePassword update) {

        return principal
                .flatMap(authentication ->
                        userRepository.isPermissionAllow(authentication.getUsername(), update.getUsername())
                                .filter(x -> x)
                                .switchIfEmpty(Mono.error(new AccessDeniedException("403 returned2")))
                )
                .switchIfEmpty(Mono.error(new AccessDeniedException("403 returned2")))
                .flatMap(authentication ->
                        userRepository.deleteByUserNameAndPassword(
                                update.getUsername()
                        )
                )
                .map(ResponseEntity::ok);
    }

    @PostMapping("/admin/account/create")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<Users> create(@AuthenticationPrincipal UserDetails principal, @Valid @RequestBody UpdatePassword newAccount) {

        System.out.println("create by: " + principal.getUsername());

        Util.ROLE role = Util.ROLE.get(newAccount.getRoles().get(0));
        Util.ROLE creatorRole = getRole(principal);

        if (role == null || creatorRole == null) {
            throw new AccessDeniedException("403 returned");
        }

        if (role == Util.ROLE.ROLE_ROOT) {
            throw new AccessDeniedException("403 returned");
        }

        if (creatorRole.GetIndex() > role.GetIndex()) {
            throw new AccessDeniedException("403 returned");
        }

        return
                Mono.just(newAccount)
                        .map(UpdatePassword::getRoles)
                        .map(roles -> Users.builder()
                                .username(newAccount.getUsername())
                                .password(this.passwordEncoder.encode(newAccount.getNewpassword()))
                                .roles(Collections.singletonList(role.getName()))
                                .createat(new Timestamp(new Date().getTime()))
                                .active(true)
                                .createby(principal.getUsername())
                                .build()
                        )
                        .flatMap(users ->
                                this.userRepository.save(users)
                        );
    }

    @PostMapping("/account/logout")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity> logout() {
        return Mono.just("")
                .map(this::createAuthBearToken);
    }
}
