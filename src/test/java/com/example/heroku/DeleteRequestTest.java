package com.example.heroku;

import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.services.DeleteRequest;
import com.example.heroku.status.ActiveStatus;
import lombok.Builder;
import reactor.test.StepVerifier;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class DeleteRequestTest {
    DeleteRequest deleteRequest;

    public void test() {
        deleteRequest.createRequest("iphone_12365");
        deleteRequest.createRequest("android_12345");
        deleteRequest.getAllRequest(SearchQuery.builder().page(0).size(1000).build())
                .sort(Comparator.comparing(com.example.heroku.model.DeleteRequest::getUser_id))
                .as(StepVerifier::create)
                .consumeNextWith(tokens -> {
                    assertThat(tokens.getUser_id()).isEqualTo("android_12345");
                    assertThat(tokens.getStatus()).isEqualTo(ActiveStatus.ACTIVE);
                })
                .consumeNextWith(tokens -> {
                    assertThat(tokens.getUser_id()).isEqualTo("iphone_12365");
                    assertThat(tokens.getStatus()).isEqualTo(ActiveStatus.ACTIVE);
                })
                .verifyComplete();
        deleteRequest.deleteByID("iphone_12365");
        deleteRequest.getAllRequest(SearchQuery.builder().page(0).size(1000).build())
                .sort(Comparator.comparing(com.example.heroku.model.DeleteRequest::getUser_id))
                .as(StepVerifier::create)
                .consumeNextWith(tokens -> {
                    assertThat(tokens.getUser_id()).isEqualTo("android_12345");
                    assertThat(tokens.getStatus()).isEqualTo(ActiveStatus.ACTIVE);
                })
                .verifyComplete();

    }
}
