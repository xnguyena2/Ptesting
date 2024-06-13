package com.example.heroku.client.payment;

import com.example.heroku.request.permission.FluxAction;
import com.example.heroku.request.permission.GetGroupID;
import com.example.heroku.request.permission.MonoAction;
import com.example.heroku.services.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PaymentValid<T> {
    private GetGroupID subject;

    private Store store;

    private FluxAction<T> fluxAction;

    private MonoAction<T> monoAction;

    private FilterAction<T> filterAction;

    boolean isStoreNotPaid(com.example.heroku.model.Store store) {
        return "SHOULD_PAID".equals(store.getPayment_status())
                || "MUST_PAID".equals(store.getPayment_status());
    }

    public Flux<T> toFluxValid() {
        return store.getStore(subject.getGroup_id())
                .flatMapMany(store1 -> {
                    if (isStoreNotPaid(store1)) {
                        throw new AccessDeniedException("403 paid services!!");
                    }
                    return fluxAction.action();
                });
    }

    public Mono<T> toMonoValid() {
        return store.getStore(subject.getGroup_id())
                .flatMap(store1 -> {
                    if (isStoreNotPaid(store1)) {
                        throw new AccessDeniedException("403 paid services!!");
                    }
                    return monoAction.action();
                });
    }

    public Flux<T> toFluxFilter() {
        return store.getStore(subject.getGroup_id())
                .flatMapMany(store1 -> {
                    if (isStoreNotPaid(store1)) {
                        return fluxAction.action().map(value -> filterAction.action(value));
                    }
                    return fluxAction.action();
                });
    }

    public Mono<T> toMonoFilter() {
        return store.getStore(subject.getGroup_id())
                .flatMap(store1 -> {
                    if (isStoreNotPaid(store1)) {
                        return monoAction.action().map(value -> filterAction.action(value));
                    }
                    return monoAction.action();
                });
    }
}
