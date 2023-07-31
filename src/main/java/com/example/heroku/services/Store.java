package com.example.heroku.services;

import com.example.heroku.model.repository.StoreManagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class Store {
    @Autowired
    StoreManagementRepository storeManagementRepository;

    public Mono<String> createStoreProductView(String group_id) {
        return storeManagementRepository.createStoreProductView(group_id)
                .then(Mono.just(group_id));
    }
}
