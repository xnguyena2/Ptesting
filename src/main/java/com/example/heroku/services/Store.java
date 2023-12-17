package com.example.heroku.services;

import com.example.heroku.model.repository.StoreManagementRepository;
import com.example.heroku.request.data.UpdatePassword;
import com.example.heroku.request.store.StoreInitData;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Component
public class Store {
    @Autowired
    private UserAccount userServices;

    @Autowired
    StoreManagementRepository storeManagementRepository;

    public Mono<String> createStoreProductView(String group_id) {
        return storeManagementRepository.createStoreProductView(group_id)
                .then(Mono.just(group_id));
    }

    public Mono<com.example.heroku.model.Store> getStore(String groupID) {
        return storeManagementRepository.getStore(groupID);
    }

    public Mono<com.example.heroku.model.Store> createOrUpdateStore(com.example.heroku.model.Store store) {
        return storeManagementRepository.updateStore(store.getGroup_id(), store.getName(), store.getTime_open(), store.getAddress(), store.getPhone(), store.getStatus(), store.getCreateat());
    }

    public Mono<com.example.heroku.model.Store> initialStore(StoreInitData storeInitData) {

        UpdatePassword newAccount = storeInitData.getNewAccount();
        System.out.println("create store and newAccount: " + newAccount.getUsername());

        newAccount.getRoles().clear();
        newAccount.getRoles().add(Util.ROLE.ROLE_ADMIN.getName());

        return initialStoreWithoutCheckRole(storeInitData);
    }

    public Mono<com.example.heroku.model.Store> initialStoreWithoutCheckRole(StoreInitData storeInitData) {

        UpdatePassword newAccount = storeInitData.getNewAccount();
        com.example.heroku.model.Store store = storeInitData.getStore().AutoFill(newAccount.getGroup_id());

        return userServices.createAccount(newAccount.getUsername(), newAccount)
                .then(createOrUpdateStore(store));
    }
}
