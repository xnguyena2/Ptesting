package com.example.heroku;

import com.example.heroku.model.Users;
import com.example.heroku.request.beer.BeerSubmitData;
import com.example.heroku.request.beer.SearchQuery;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class UserAccountTest {


    @Value("${account.admin.username}")
    private String adminName;


    com.example.heroku.services.UserAccount userAccount;

    public void test(){

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
