package com.example.heroku.request.permission;


import com.example.heroku.model.Users;
import com.example.heroku.request.beer.SearchQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WrapPermissionAction<T> {
    private Mono<Users> principal;

    private SearchQuery query;

    private DoQueryFlux<T> fluxAction;

    private DoQueryMono<T> monoAction;

    public Flux<T> toFlux() {
        return principal
                .filter(users -> query.getGroup_id().startsWith(users.getGroup_id()))
                .flatMapMany(users -> fluxAction.doQueryFlux(query));
    }

    public Mono<T> toMono() {
        return principal
                .filter(users -> query.getGroup_id().contains(users.getGroup_id()))
                .flatMap(users -> monoAction.doQueryMono(query));
    }
}