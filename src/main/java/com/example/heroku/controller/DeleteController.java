package com.example.heroku.controller;

import com.example.heroku.services.DeleteAllData;
import com.example.heroku.services.UserAccount;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/delete")
public class DeleteController {

    @Autowired
    private UserAccount userServices;

    @Autowired
    private DeleteAllData deleteAllData;

    @PostMapping("/admin/account/delete")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity> selfMarkDelete(@AuthenticationPrincipal Mono<UserDetails> principal) {

        try {
            return principal
                    .flatMap(login -> deleteAllData.seftMarkDelete(
                                    login.getUsername()
                            )
                    )
                    .map(ResponseEntity::ok);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }
}
