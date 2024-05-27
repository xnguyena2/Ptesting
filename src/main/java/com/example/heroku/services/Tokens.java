package com.example.heroku.services;

import com.example.heroku.model.repository.TokensRepository;
import com.example.heroku.request.beer.BeerSubmitData;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.carousel.IDContainer;
import com.example.heroku.status.ActiveStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class Tokens {

    @Autowired
    TokensRepository tokensRepository;


    public Mono<com.example.heroku.model.Tokens> createToken(String groupID, String tokenID, String token, String username, long expired) {
        com.example.heroku.model.Tokens tokens = com.example.heroku.model.Tokens.builder()
                .token_second_id(tokenID)
                .token(token)
                .username(username)
                .expire(expired)
                .status(ActiveStatus.ACTIVE)
                .build();
        tokens.AutoFill(groupID);
        return tokensRepository.save(tokens).then(Mono.just(tokens));
    }

    public Mono<com.example.heroku.model.Tokens> getToken(String tokenID) {
        return tokensRepository.getById(tokenID);
    }

    public Flux<com.example.heroku.model.Tokens> getAllToken(SearchQuery query) {
        return this.tokensRepository.findByIdNotNull(query.getGroup_id(), query.getPage(), query.getSize());
    }

    public Flux<com.example.heroku.model.Tokens> getAllTokenByUserID(SearchQuery query) {
        return this.tokensRepository.getByUserID(query.getGroup_id(), query.getQuery());
    }

    public Mono<com.example.heroku.model.Tokens> changeTokensStatus(String groupID, String tokenID, String status) {
        return this.tokensRepository.updateStatus(groupID, tokenID, ActiveStatus.get(status));
    }

    public Mono<com.example.heroku.model.Tokens> deleteAllToken(String gorupID) {
        return this.tokensRepository.deleteByGroupID(gorupID);
    }

    public Mono<com.example.heroku.model.Tokens> deleteByID(String gorupID, String id) {
        return this.tokensRepository.deleteByID(gorupID, id);
    }

    public Mono<com.example.heroku.model.Tokens> deleteByUserID(String gorupID, String id) {
        return this.tokensRepository.deleteByUserID(gorupID, id);
    }

    public Mono<Boolean> isCorrectStatus(String token, ActiveStatus status) {
        return this.tokensRepository.isTokenMatchStatus(token, status);
    }
}
