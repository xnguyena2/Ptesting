package com.example.heroku;

import com.example.heroku.model.Users;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.services.Store;
import com.example.heroku.services.Tokens;
import com.example.heroku.status.ActiveStatus;
import lombok.Builder;
import reactor.test.StepVerifier;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class TokenTest {
    Tokens tokens;

    String group;

    public void test() {
        tokens.createToken(group,group+"token_1", group+"ttttttt").block();
        tokens.createToken(group,group+"token_2", "aaaaaaa").block();
        tokens.createToken(group,group+"token_3", "bbbbbbb").block();
        tokens.getAllToken(SearchQuery.builder().group_id(group).page(0).size(1000).build())
                .sort(Comparator.comparing(com.example.heroku.model.Tokens::getToken_second_id))
                .as(StepVerifier::create)
                .consumeNextWith(tokens -> {
                    assertThat(tokens.getGroup_id()).isEqualTo(group);
                    assertThat(tokens.getToken_second_id()).isEqualTo(group+"token_1");
                    assertThat(tokens.getToken()).isEqualTo(group+"ttttttt");
                    assertThat(tokens.getStatus()).isEqualTo(ActiveStatus.ACTIVE);
                })
                .consumeNextWith(tokens -> {
                    assertThat(tokens.getGroup_id()).isEqualTo(group);
                    assertThat(tokens.getToken_second_id()).isEqualTo(group+"token_2");
                    assertThat(tokens.getToken()).isEqualTo("aaaaaaa");
                    assertThat(tokens.getStatus()).isEqualTo(ActiveStatus.ACTIVE);
                })
                .consumeNextWith(tokens -> {
                    assertThat(tokens.getGroup_id()).isEqualTo(group);
                    assertThat(tokens.getToken_second_id()).isEqualTo(group+"token_3");
                    assertThat(tokens.getToken()).isEqualTo("bbbbbbb");
                    assertThat(tokens.getStatus()).isEqualTo(ActiveStatus.ACTIVE);
                })
                .verifyComplete();
        tokens.changeTokensStatus(group, group+"token_2", ActiveStatus.DE_ACTIVE.toString()).block();
        tokens.getToken(group+"token_2")
                .as(StepVerifier::create)
                .consumeNextWith(tokens -> {
                    assertThat(tokens.getGroup_id()).isEqualTo(group);
                    assertThat(tokens.getToken_second_id()).isEqualTo(group+"token_2");
                    assertThat(tokens.getToken()).isEqualTo("aaaaaaa");
                    assertThat(tokens.getStatus()).isEqualTo(ActiveStatus.DE_ACTIVE);
                })
                .verifyComplete();
        tokens.deleteByID(group, group+"token_3").block();
        tokens.getToken(group+"token_3")
                .as(StepVerifier::create)
                .verifyComplete();
        tokens.isCorrectStatus(group+"ttttttt", ActiveStatus.ACTIVE)
                .as(StepVerifier::create)
                .consumeNextWith(status -> {
                    assertThat(status).isEqualTo(Boolean.TRUE);
                })
                .verifyComplete();
        tokens.getAllToken(SearchQuery.builder().group_id(group).page(0).size(1000).build())
                .sort(Comparator.comparing(com.example.heroku.model.Tokens::getToken_second_id))
                .as(StepVerifier::create)
                .consumeNextWith(tokens -> {
                    assertThat(tokens.getGroup_id()).isEqualTo(group);
                    assertThat(tokens.getToken_second_id()).isEqualTo(group+"token_1");
                    assertThat(tokens.getToken()).isEqualTo(group+"ttttttt");
                    assertThat(tokens.getStatus()).isEqualTo(ActiveStatus.ACTIVE);
                })
                .consumeNextWith(tokens -> {
                    assertThat(tokens.getGroup_id()).isEqualTo(group);
                    assertThat(tokens.getToken_second_id()).isEqualTo(group+"token_2");
                    assertThat(tokens.getToken()).isEqualTo("aaaaaaa");
                    assertThat(tokens.getStatus()).isEqualTo(ActiveStatus.DE_ACTIVE);
                })
                .verifyComplete();
        tokens.deleteAllToken(group).block();
        tokens.getAllToken(SearchQuery.builder().group_id(group).page(0).size(1000).build())
                .sort(Comparator.comparing(com.example.heroku.model.Tokens::getToken_second_id))
                .as(StepVerifier::create)
                .verifyComplete();

    }
}
