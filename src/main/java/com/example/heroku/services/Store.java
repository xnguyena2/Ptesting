package com.example.heroku.services;

import com.example.heroku.model.repository.StoreManagementRepository;
import com.example.heroku.request.data.UpdatePassword;
import com.example.heroku.request.store.StoreInitData;
import com.example.heroku.request.warehouse.SearchImportQuery;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    public Flux<com.example.heroku.model.Store> findStore(String searchTxt) {
        return storeManagementRepository.findStore("%" + searchTxt.toLowerCase() + "%");
    }

    public Flux<com.example.heroku.model.Store> getAllPaid() {
        return storeManagementRepository.getAllPaid();
    }

    public Flux<com.example.heroku.model.Store> getAllStoreBaseonDonePackage(String metaSearch) {
        return storeManagementRepository.searchStoreNotPaidOfDonePackageDetailMetaSearch("%" + metaSearch + "%");
    }

    public Flux<com.example.heroku.model.Store> getAllStoreCreateBetween(SearchImportQuery searchImportQuery) {
        return storeManagementRepository.getAllStoreCreateBetween(searchImportQuery.getFrom(), searchImportQuery.getTo());
    }

    public Flux<com.example.heroku.model.Store> getAllStore(SearchImportQuery searchImportQuery) {
        Sort.Direction direction = "DESC".equals(searchImportQuery.getSearch_txt()) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(
                searchImportQuery.getPage(),
                searchImportQuery.getSize(),
                Sort.by(direction, "createat")
        );
        return storeManagementRepository.findAll(pageable);
    }

    public Mono<com.example.heroku.model.Store> getStoreDomainUrl(String domain) {
        return storeManagementRepository.getStoreDomainUrl(domain);
    }

    public Mono<com.example.heroku.model.Store> createOrUpdateStore(com.example.heroku.model.Store store) {
        return storeManagementRepository.insertOrUpdate(store.getGroup_id(), store.getName(), store.getTime_open(), store.getAddress(), store.getPhone(), store.getDomain_url(), store.getStatus(), store.getStore_type(), store.getCreateat());
    }

    public Mono<com.example.heroku.model.Store> update(com.example.heroku.model.Store store) {
        return storeManagementRepository.update(store.getGroup_id(), store.getName(), store.getTime_open(), store.getAddress(), store.getPhone(), store.getDomain_url(), store.getStatus(), store.getStore_type());
    }

    public Mono<com.example.heroku.model.Store> updatePaymentStatus(String groupID, String paymentStatus) {
        return storeManagementRepository.updatePaymentStatus(groupID, paymentStatus);
    }

    public Mono<com.example.heroku.model.Store> initialStore(StoreInitData storeInitData) {

        UpdatePassword newAccount = storeInitData.getNewAccount();
        System.out.println("create store and newAccount: " + newAccount.getUsername());

        newAccount.getRoles().clear();
        newAccount.getRoles().add(Util.ROLE.ROLE_ADMIN.getName());

        return initialStoreWithoutCheckRole(storeInitData)
                .onErrorResume(throwable -> {
                            String msg = throwable.getMessage();
                            System.out.println(msg);
                            if (!msg.contains("uq_users_name")) {
                                throw new RuntimeException(throwable);
                            }
                            return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg));
                        }
                );
    }

    public Mono<com.example.heroku.model.Store> initialStoreWithoutCheckRole(StoreInitData storeInitData) {

        UpdatePassword newAccount = storeInitData.getNewAccount();
        com.example.heroku.model.Store store = storeInitData.getStore().AutoFill(newAccount.getGroup_id());

        return userServices.createAccount(newAccount.getUsername(), newAccount)
                .then(createOrUpdateStore(store));
    }
}
