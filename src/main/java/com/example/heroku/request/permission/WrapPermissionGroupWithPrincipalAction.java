package com.example.heroku.request.permission;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class WrapPermissionGroupWithPrincipalAction<T> {
    private Mono<? extends GetGroupID> principal;
//    private GetGroupID principal;
    private GetGroupID subject;

    private FluxAction<T> fluxAction;

    private MonoAction<T> monoAction;

    public Flux<T> toFlux() {
        return principal
                .filter(users -> subject.getGroup_id().startsWith(users.getGroup_id()))
                .flatMapMany(users -> fluxAction.action());
    }

    public Mono<T> toMono() {
        return principal
                .filter(users -> subject.getGroup_id().startsWith(users.getGroup_id()))
                .flatMap(users -> monoAction.action());
    }
}
