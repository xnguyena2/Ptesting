package com.example.heroku;

import com.example.heroku.model.Users;
import com.example.heroku.request.beer.BeerSubmitData;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.data.UpdatePassword;
import com.example.heroku.services.UsersInfo;
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

    UsersInfo usersInfo;

    String group;

    public void test() {

        this.userAccount.createAccount(adminName, UpdatePassword.builder()
                .username("phong test")
                .newpassword("phong test pass")
                .group_id(group)
                .phone_number("121")
                .title("giam doc")
                .roles_in_group("r1,r2")
                .roles(Collections.singletonList(Util.ROLE.ROLE_ADMIN.getName()))
                .register_code("hello code")
                .build()).block();

        this.userAccount.createAccount(adminName, UpdatePassword.builder()
                .username("phong test")
                .newpassword("phong test pass")
                .group_id(group)
                .phone_number("121")
                .title("giam doc")
                .roles_in_group("r1,r2")
                .roles(Collections.singletonList(Util.ROLE.ROLE_ADMIN.getName()))
                .register_code("hello code")
                .update_password(true)
                .build()).block();

        this.userAccount.createAccount(adminName, UpdatePassword.builder()
                .username("pong test")
                .newpassword("phong test pass")
                .group_id(group)
                .phone_number("121")
                .full_name("pong")
                .title("giam doc")
                .roles_in_group("r1,r2")
                .roles(Collections.singletonList(Util.ROLE.ROLE_ADMIN.getName()))
                .build()).block();

        usersInfo.getAllUsersInfo(SearchQuery.builder().page(0).size(100).group_id(group).build())
                .sort(Comparator.comparing(com.example.heroku.model.UsersInfo::getUsername))
                .as(StepVerifier::create)
                .consumeNextWith(usersInfo -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(usersInfo));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(usersInfo.getUsername()).isEqualTo("binhdiepquin");
                    assertThat(usersInfo.getUser_fullname()).isEqualTo(null);
                    assertThat(usersInfo.getPhone()).isEqualTo(null);
                    assertThat(usersInfo.getTitle()).isEqualTo(null);
                    assertThat(usersInfo.getRoles()).isEqualTo(null);
                })
                .consumeNextWith(usersInfo -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(usersInfo));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(usersInfo.getUsername()).isEqualTo("phong test");
                    assertThat(usersInfo.getUser_fullname()).isEqualTo(null);
                    assertThat(usersInfo.getPhone()).isEqualTo("121");
                    assertThat(usersInfo.getTitle()).isEqualTo("giam doc");
                    assertThat(usersInfo.getRoles()).isEqualTo("r1,r2");
                })
                .consumeNextWith(usersInfo -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(usersInfo));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(usersInfo.getUser_fullname()).isEqualTo("pong");
                    assertThat(usersInfo.getPhone()).isEqualTo("121");
                    assertThat(usersInfo.getTitle()).isEqualTo("giam doc");
                    assertThat(usersInfo.getRoles()).isEqualTo("r1,r2");
                })
                .verifyComplete();

        userAccount.getAllUserClean(SearchQuery.builder().page(0).size(100).group_id(group).build())
                .sort(Comparator.comparing(com.example.heroku.model.joinwith.UserJoinUserInfo::getUsername))
                .as(StepVerifier::create)
                .consumeNextWith(usersInfo -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(usersInfo));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(usersInfo.getUsername()).isEqualTo("binhdiepquin");
                    assertThat(usersInfo.getUser_fullname()).isEqualTo(null);
                    assertThat(usersInfo.getPhone()).isEqualTo(null);
                    assertThat(usersInfo.getTitle()).isEqualTo(null);
                    assertThat(usersInfo.getClient_roles()).isEqualTo(null);
                })
                .consumeNextWith(usersInfo -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(usersInfo));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(usersInfo.getUsername()).isEqualTo("phong test");
                    assertThat(usersInfo.getUser_fullname()).isEqualTo(null);
                    assertThat(usersInfo.getPhone()).isEqualTo("121");
                    assertThat(usersInfo.getTitle()).isEqualTo("giam doc");
                    assertThat(usersInfo.getClient_roles()).isEqualTo("r1,r2");
                })
                .consumeNextWith(usersInfo -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(usersInfo));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(usersInfo.getUser_fullname()).isEqualTo("pong");
                    assertThat(usersInfo.getPhone()).isEqualTo("121");
                    assertThat(usersInfo.getTitle()).isEqualTo("giam doc");
                    assertThat(usersInfo.getClient_roles()).isEqualTo("r1,r2");
                })
                .verifyComplete();

        this.userAccount.getAll(SearchQuery.builder().page(0).size(100).group_id(group).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(3);
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
                            .consumeNextWith(users -> {
                                assertThat(users.getUsername()).isEqualTo("pong test");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.userAccount.getUserClean("phong test")
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getUsername()).isEqualTo("phong test");
                    assertThat(resultWithCount.getPassword()).isEqualTo(null);
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

        this.userAccount.deleteAccount(Mono.just(Users.builder().username(adminName).group_id(group).build()), UpdatePassword.builder().username("phong test").build()).block();

        this.userAccount.getUser("phong test")
                .as(StepVerifier::create)
                .verifyComplete();

        this.userAccount.createAccount(adminName, UpdatePassword.builder()
                .username("phong test 2")
                .newpassword("phong test pass")
                .group_id(group)
                .roles(Collections.singletonList(Util.ROLE.ROLE_ADMIN.getName()))
                .build()).block();

        this.userAccount.getAll(SearchQuery.builder().page(0).size(100).group_id(group).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(3);
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
                            .consumeNextWith(users -> {
                                assertThat(users.getUsername()).isEqualTo("pong test");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.userAccount.seftDelete("phong test 2").block();

        this.userAccount.getAll(SearchQuery.builder().page(0).size(100).group_id(group).build())
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
                                assertThat(users.getUsername()).isEqualTo("pong test");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.userAccount.deleteAccount(Mono.just(Users.builder().username(adminName).group_id(group).build()), UpdatePassword.builder().username("pong test").build()).block();

        this.userAccount.getAll(SearchQuery.builder().page(0).size(100).group_id(group).build())
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

    public void test2(){

        this.userAccount.createAccount(adminName, UpdatePassword.builder()
                .username("phong test2")
                .newpassword("phong test pass")
                .group_id(group)
                .roles(Collections.singletonList(Util.ROLE.ROLE_ADMIN.getName()))
                .build()).block();

        this.userAccount.getAll(SearchQuery.builder().page(0).size(100).group_id(group).build())
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
                                assertThat(users.getUsername()).isEqualTo("phong test2");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.userAccount.getUserClean("phong test2")
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getUsername()).isEqualTo("phong test2");
                    assertThat(resultWithCount.getPassword()).isEqualTo(null);
                })
                .verifyComplete();

        this.userAccount.getUser("phong test2")
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getUsername()).isEqualTo("phong test2");
                    assertThat(passwordEncoder.matches("phong test pass", resultWithCount.getPassword())).isEqualTo(true);
                })
                .verifyComplete();

        this.userAccount.updatePassword("phong test2", "phong update pass2").block();

        this.userAccount.getUser("phong test2")
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getUsername()).isEqualTo("phong test2");
                    assertThat(passwordEncoder.matches("phong update pass2", resultWithCount.getPassword())).isEqualTo(true);
                })
                .verifyComplete();

        this.userAccount.deleteAccount(Mono.just(Users.builder().username(adminName).group_id(group).build()), UpdatePassword.builder().username("phong test2").build()).block();

        this.userAccount.getUser("phong test2")
                .as(StepVerifier::create)
                .verifyComplete();

        this.userAccount.createAccount(adminName, UpdatePassword.builder()
                .username("phong test 22")
                .newpassword("phong test pass")
                .group_id(group)
                .roles(Collections.singletonList(Util.ROLE.ROLE_ADMIN.getName()))
                .build()).block();

        this.userAccount.getAll(SearchQuery.builder().page(0).size(100).group_id(group).build())
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
                                assertThat(users.getUsername()).isEqualTo("phong test 22");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.userAccount.seftDelete("phong test 22").block();

        this.userAccount.getAll(SearchQuery.builder().page(0).size(100).group_id(group).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(0);
                    assertThat(resultWithCount.getResult()).isEqualTo(null);
                })
                .verifyComplete();
    }
}
