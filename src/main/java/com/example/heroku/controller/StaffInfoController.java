package com.example.heroku.controller;

import com.example.heroku.jwt.JwtTokenProvider;
import com.example.heroku.model.Tokens;
import com.example.heroku.model.Users;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.carousel.IDContainer;
import com.example.heroku.request.data.UpdatePassword;
import com.example.heroku.request.permission.WrapPermissionGroupWithPrincipalAction;
import com.example.heroku.services.UserAccount;
import com.example.heroku.services.UsersInfo;
import com.example.heroku.status.ActiveStatus;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/staff")
public class StaffInfoController {

    @Autowired
    private UsersInfo usersInfoServices;

    @Autowired
    private UserAccount userServices;


    @PostMapping("/admin/getall")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<com.example.heroku.model.UsersInfo> getAll(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid SearchQuery searchQuery) {
        return usersInfoServices.getAllUsersInfo(searchQuery);
    }


    @PostMapping("/admin/update")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<com.example.heroku.model.UsersInfo> update(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid com.example.heroku.model.UsersInfo usersInfo) {

        return WrapPermissionGroupWithPrincipalAction.<com.example.heroku.model.UsersInfo>builder()
                .principal(principal)
                .subject(usersInfo::getGroup_id)
                .monoAction(() -> usersInfoServices.createUserInfo(usersInfo))
                .build().toMono();
    }


    @GetMapping("/admin/getby/{username}")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<com.example.heroku.model.UsersInfo> get(@AuthenticationPrincipal Mono<Users> principal, @PathVariable("username") String username) {

        return principal.flatMap(users -> usersInfoServices.findByUsername(username).filter(usersInfo -> usersInfo.getGroup_id().equals(users.getGroup_id())));
    }


    @PostMapping("/admin/delete")
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
}
