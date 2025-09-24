package com.example.heroku.controller;

import com.example.heroku.jwt.JwtTokenProvider;
import com.example.heroku.model.Tokens;
import com.example.heroku.model.Users;
import com.example.heroku.model.repository.UserRepository;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.carousel.IDContainer;
import com.example.heroku.request.client.TokenID;
import com.example.heroku.request.permission.WrapPermissionGroupWithPrincipalAction;
import com.example.heroku.status.ActiveStatus;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @Autowired
    UserRepository userRepository;


    @PostMapping("/admin/create")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<Tokens> createTokenForStaff(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid TokenID tokenID) {

        System.out.println("admin Create token for user: " + tokenID.getUser_id() + " in group: " + tokenID.getGroup_id() + ", with time life: " + tokenID.getTime_life_mili_secs());

        return userRepository.findByUsername(tokenID.getUser_id())
                .map(u -> {
                            u.setPassword(u.getPassword().trim());
                            return u.parseauthorities();
                        }
                )
                .flatMap(users -> WrapPermissionGroupWithPrincipalAction.<Tokens>builder()
                        .principal(principal)
                        .subject(users::getGroup_id)
                        .monoAction(() -> Mono.just(jwtTokenProvider.createToken(users, tokenID.getTime_life_mili_secs()))
                                .flatMap(s -> tokens.createToken(users.getGroup_id(), tokenID.getToken_id(), s, tokenID.getUser_id(), tokenID.getTime_life_mili_secs()))
                        )
                        .build().toMono()
                );
    }


    @PostMapping("/create")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<Tokens> createToken(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid IDContainer idContainer) {
        System.out.println("Create token for user: " + idContainer.getId() + " in group: " + idContainer.getGroup_id());
        return principal.flatMap(users -> userRepository.findByUsername(users.getUsername())
                        .map(u -> {
                                    u.setPassword(u.getPassword().trim());
                                    return u.parseauthorities();
                                }
                        ))
                .flatMap(users -> WrapPermissionGroupWithPrincipalAction.<Tokens>builder()
                        .principal(principal)
                        .subject(idContainer::getGroup_id)
                        .monoAction(() -> Mono.just(this.jwtTokenProvider.createToken(users))
                                .flatMap(s -> tokens.createToken(idContainer.getGroup_id(), idContainer.getId(), s, users.getUsername(), 0)
                                )
                        )
                        .build().toMono());
    }


    @PostMapping("/delete")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<Tokens> deleteToken(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid IDContainer idContainer) {

        System.out.println("Delete token for user: " + idContainer.getId() + " in group: " + idContainer.getGroup_id());
        return WrapPermissionGroupWithPrincipalAction.<Tokens>builder()
                .principal(principal)
                .subject(idContainer::getGroup_id)
                .monoAction(() -> tokens.deleteByID(idContainer.getGroup_id(), idContainer.getId()))
                .build().toMono();
    }


    @PostMapping("/deletebyuser")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<Tokens> deleteTokenByUser(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid IDContainer idContainer) {

        System.out.println("Delete all token of user: " + idContainer.getId() + " in group: " + idContainer.getGroup_id());
        return WrapPermissionGroupWithPrincipalAction.<Tokens>builder()
                .principal(principal)
                .subject(idContainer::getGroup_id)
                .monoAction(() -> principal.flatMap(users -> tokens.deleteByUserID(idContainer.getGroup_id(), idContainer.getId())))
                .build().toMono();
    }


    @PostMapping("/markinvalid")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<Tokens> invalidToken(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid IDContainer idContainer) {

        System.out.println("Mark invalid token for user: " + idContainer.getId() + " in group: " + idContainer.getGroup_id());
        return WrapPermissionGroupWithPrincipalAction.<Tokens>builder()
                .principal(principal)
                .subject(idContainer::getGroup_id)
                .monoAction(() -> tokens.changeTokensStatus(idContainer.getGroup_id(), idContainer.getId(), ActiveStatus.DE_ACTIVE.toString()))
                .build().toMono();
    }

    @PostMapping("/getall")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<Tokens> getAllToken(@AuthenticationPrincipal Users principal, @RequestBody @Valid SearchQuery query) {
        System.out.println("Get all token with query: " + query.getGroup_id());
        return tokens.getAllToken(query);
    }

    @PostMapping("/getallbyuser")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<Tokens> getAllTokenByUserID(@AuthenticationPrincipal Users principal, @RequestBody @Valid SearchQuery query) {
        System.out.println("Get all token of user: " + query.getQuery() + " in group: " + query.getGroup_id());
        return tokens.getAllTokenByUserID(query);
    }

    @GetMapping("/get/{tokenid}")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<Tokens> getToken(@PathVariable("tokenid") String tokenid) {
        System.out.println("Get token by id: " + tokenid);
        return tokens.getToken(tokenid);
    }
}
