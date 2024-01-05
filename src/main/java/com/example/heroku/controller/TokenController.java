package com.example.heroku.controller;

import com.example.heroku.jwt.JwtTokenProvider;
import com.example.heroku.model.Image;
import com.example.heroku.model.Tokens;
import com.example.heroku.model.Users;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.carousel.IDContainer;
import com.example.heroku.request.permission.WrapPermissionGroupWithPrincipalAction;
import com.example.heroku.status.ActiveStatus;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/token")
public class TokenController {

    @Autowired
    private com.example.heroku.services.Tokens tokens;

    @Autowired
    JwtTokenProvider jwtTokenProvider;


    @PostMapping("/create")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<Tokens> createToken(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid IDContainer idContainer) {

        return WrapPermissionGroupWithPrincipalAction.<Tokens>builder()
                .principal(principal)
                .subject(idContainer::getGroup_id)
                .monoAction(() -> principal
                        .map(this.jwtTokenProvider::createToken)
                        .flatMap(s -> tokens.createToken(idContainer.getGroup_id(), idContainer.getId(), s))
                )
                .build().toMono();
    }


    @PostMapping("/delete")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<Tokens> deleteToken(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid IDContainer idContainer) {

        return WrapPermissionGroupWithPrincipalAction.<Tokens>builder()
                .principal(principal)
                .subject(idContainer::getGroup_id)
                .monoAction(() -> tokens.deleteByID(idContainer.getGroup_id(), idContainer.getId()))
                .build().toMono();
    }


    @PostMapping("/markinvalid")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<Tokens> invalidToken(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid IDContainer idContainer) {

        return WrapPermissionGroupWithPrincipalAction.<Tokens>builder()
                .principal(principal)
                .subject(idContainer::getGroup_id)
                .monoAction(() -> tokens.changeTokensStatus(idContainer.getGroup_id(), idContainer.getId(), ActiveStatus.DE_ACTIVE.toString()))
                .build().toMono();
    }

    @PostMapping("/getall/{groupid}")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<Tokens> getAllToken(@RequestBody @Valid SearchQuery query) {
        return tokens.getAllToken(query);
    }

    @GetMapping("/get/{tokenid}")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<Tokens> getToken(@PathVariable("tokenid") String tokenid) {
        return tokens.getToken(tokenid);
    }
}
