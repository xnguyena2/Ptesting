package com.example.heroku;


import com.example.heroku.jwt.JwtTokenProvider;
import com.example.heroku.model.Tokens;
import com.example.heroku.model.Users;
import com.example.heroku.model.joinwith.UserJoinUserInfo;
import com.example.heroku.request.data.AuthenticationRequest;
import com.example.heroku.request.data.UpdatePassword;
import com.example.heroku.request.permission.WrapPermissionGroupWithPrincipalAction;
import com.example.heroku.response.Format;
import com.example.heroku.services.DeleteAllData;
import com.example.heroku.services.UserAccount;
import com.example.heroku.util.Util;
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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

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
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserAccount userServices;

    @Autowired
    private DeleteAllData deleteAllData;

    @Autowired
    private com.example.heroku.services.Tokens tokens;

    private ResponseEntity createAuthBearToken(String jwt) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
        Tokens tokens = Tokens.builder()
                .group_id("_not_set_")
                .token(jwt)
                .build();
        ResponseCookie cookie = ResponseCookie.from(accessTokenCookieName, jwt)
                .path("/")
                .httpOnly(httponlysecure)
                .secure(httponlysecure)///// must true
                .maxAge(validityInMs)
                .build();
        httpHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString());
        return new ResponseEntity<>(tokens, httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/signin")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity> signin(@Valid @RequestBody AuthenticationRequest authRequest) {
        System.out.println("request signin from username: " + authRequest.getUsername());
        try {
            return this.authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()))
                    .map(this.jwtTokenProvider::createToken)
                    .map(this::createAuthBearToken);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

    @GetMapping("/signinwithtoken/{tokenid}")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity> signinWithToken(@PathVariable("tokenid") String tokenid) {
        System.out.println("request signin with token: " + tokenid);
        return tokens.getToken(tokenid).map(tokens1 -> createAuthBearToken(tokens1.getToken()));
    }

    @PostMapping("/account/create")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<Users> signup(@Valid @RequestBody UpdatePassword newAccount) {

        System.out.println("create newAccount: " + newAccount.getUsername());

        newAccount.getRoles().clear();
        newAccount.getRoles().add(Util.ROLE.ROLE_ADMIN.getName());

        return userServices.createAccount(newAccount.getUsername(), newAccount);
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
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<Map<String, Object>> current(@AuthenticationPrincipal Mono<Users> principal) {
        return principal
                .map(Users::getJsonObject);
    }

    @PostMapping("/account/updatepassword")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity> update(@AuthenticationPrincipal Mono<UserDetails> principal, @Valid @RequestBody UpdatePassword update) {

        try {
            return principal
                    .flatMap(login -> this.authenticationManager
                            .authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), update.getOldpassword()))
                            .flatMap(authentication ->
                                    userServices.updatePassword(login.getUsername(), update.getNewpassword())
                            )
                    )
                    .then(Mono.just(""))
                    .map(this::createAuthBearToken);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

    @PostMapping("/admin/account/updatepassword")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity> updatePassword(@AuthenticationPrincipal Mono<UserDetails> principal, @Valid @RequestBody UpdatePassword update) {

        try {
            return principal
                    .flatMap(
                            login -> userServices.updatePassword(login.getUsername(), update.getNewpassword())
                    )
                    .then(Mono.just(ok(Format.builder().response("done").build())));
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
                                    authentication.getName().equals(update.getUsername())
                            )
                            .switchIfEmpty(Mono.error(new AccessDeniedException("403 returned1")))
                            .flatMap(authentication ->
                                    userServices.seftDelete(
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

    @PostMapping("/account/markdelete")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity> selfMarkDelete(@AuthenticationPrincipal Mono<UserDetails> principal) {

        try {
            return principal
                    .filter(userDetails -> getRole(userDetails) == Util.ROLE.ROLE_ADMIN)
                    .switchIfEmpty(Mono.error(new AccessDeniedException("403 returned1")))
                    .flatMap(login -> deleteAllData.seftMarkDelete(
                                    login.getUsername()
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
    public Mono<ResponseEntity> delete(@AuthenticationPrincipal Mono<Users> principal, @Valid @RequestBody UpdatePassword update) {

        return userServices.getUser(update.getUsername())
                .flatMap(users ->
                        WrapPermissionGroupWithPrincipalAction.<ResponseEntity>builder()
                                .principal(principal)
                                .subject(users::getGroup_id)
                                .monoAction(() -> userServices.deleteAccount(principal, update))
                                .build().toMono()
                );
    }

    @GetMapping("/account/info")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<UserJoinUserInfo> getAccountInfo(@AuthenticationPrincipal Mono<Users> principal) {
        return principal.flatMap(users -> userServices.getUserClean(users.getUsername()).map(userJoinUserInfo -> {
            if (!userJoinUserInfo.isActive()) {
                throw new AccessDeniedException("403 account disable!!");
            }
            return userJoinUserInfo;
        }));
    }

    @PostMapping("/admin/account/create")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<Users> create(@AuthenticationPrincipal Users principal, @Valid @RequestBody UpdatePassword newAccount) {

        System.out.println("create by: " + principal.getUsername());

        Util.ROLE role = Util.ROLE.get(newAccount.getRoles().get(0));
        Util.ROLE creatorRole = getRole(principal);

        //we must allow only admin can create user because this can got hack, because before create user it delete anyway
//        if (!principal.getGroup_id().endsWith("_" + principal.getUsername())) {
//            throw new AccessDeniedException("you not admin!!");
//        }

        if (role == null || creatorRole == null) {
            throw new AccessDeniedException("403 creator or new account role is null!!");
        }

        if (role == Util.ROLE.ROLE_ROOT) {
            throw new AccessDeniedException("403 can not create root role!!");
        }

        if (creatorRole.GetIndex() > role.GetIndex()) {
            throw new AccessDeniedException("403 no permission!!");
        }

        return WrapPermissionGroupWithPrincipalAction.<Users>builder()
                .principal(Mono.just(principal))
                .subject(newAccount::getGroup_id)
                .monoAction(() -> userServices.createAccountByAdmin(principal.getUsername(), newAccount))
                .build().toMono();
    }

    @PostMapping("/account/logout")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity> logout() {
        return Mono.just("")
                .map(this::createAuthBearToken);
    }
}
