package com.example.heroku;

import com.example.heroku.model.Users;
import com.example.heroku.request.beer.BeerSubmitData;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.data.UpdatePassword;
import com.example.heroku.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class UserAccountTest {


    private String adminName;

    PasswordEncoder passwordEncoder;

    com.example.heroku.services.UserAccount userAccount;

    public void test(){

        this.userAccount.createAccount(adminName, UpdatePassword.builder()
                        .username("phong test")
                        .newpassword("phong test pass")
                        .group_id(Config.group)
                        .roles(Collections.singletonList(Util.ROLE.ROLE_ADMIN.getName()))
                .build()).block();

        this.userAccount.getAll(SearchQuery.builder().page(0).size(100).group_id(Config.group).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(2);
                    Mono.just(resultWithCount.getResult())
                            .flatMapMany(Flux::fromIterable)
                            .sort(Comparator.comparing(Users::getUsername))
                            .as(StepVerifier::create)
                            .consumeNextWith(users -> {
                                assertThat(users.getUsername()).isEqualTo(adminName);
                            })
                            .consumeNextWith(users -> {
                                assertThat(users.getUsername()).isEqualTo("phong test");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.userAccount.getUser("phong test")
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getUsername()).isEqualTo("phong test");
                    assertThat(passwordEncoder.matches("phong test pass", resultWithCount.getPassword())).isEqualTo(true);
                })
                .verifyComplete();

        this.userAccount.updatePassword("phong test", "phong update pass").block();

        this.userAccount.getUser("phong test")
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getUsername()).isEqualTo("phong test");
                    assertThat(passwordEncoder.matches("phong update pass", resultWithCount.getPassword())).isEqualTo(true);
                })
                .verifyComplete();

        this.userAccount.deleteAccount(Mono.just(Users.builder().username(adminName).build()), UpdatePassword.builder().username("phong test").build()).block();

        this.userAccount.getUser("phong test")
                .as(StepVerifier::create)
                .verifyComplete();

        this.userAccount.createAccount(adminName, UpdatePassword.builder()
                .username("phong test 2")
                .newpassword("phong test pass")
                .group_id(Config.group)
                .roles(Collections.singletonList(Util.ROLE.ROLE_ADMIN.getName()))
                .build()).block();

        this.userAccount.getAll(SearchQuery.builder().page(0).size(100).group_id(Config.group).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(2);
                    Mono.just(resultWithCount.getResult())
                            .flatMapMany(Flux::fromIterable)
                            .sort(Comparator.comparing(Users::getUsername))
                            .as(StepVerifier::create)
                            .consumeNextWith(users -> {
                                assertThat(users.getUsername()).isEqualTo(adminName);
                            })
                            .consumeNextWith(users -> {
                                assertThat(users.getUsername()).isEqualTo("phong test 2");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.userAccount.seftDelete("phong test 2").block();

        this.userAccount.getAll(SearchQuery.builder().page(0).size(100).group_id(Config.group).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(1);
                    Mono.just(resultWithCount.getResult())
                            .flatMapMany(Flux::fromIterable)
                            .sort(Comparator.comparing(Users::getUsername))
                            .as(StepVerifier::create)
                            .consumeNextWith(users -> {
                                assertThat(users.getUsername()).isEqualTo(adminName);
                            })
                            .verifyComplete();
                })
                .verifyComplete();
    }
}
