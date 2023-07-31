package com.example.heroku;

import com.example.heroku.services.Store;
import lombok.Builder;

@Builder
public class StoreTest {
    Store storeServices;

    String group;
    public void test(){
        storeServices.createStoreProductView(group).block();
    }
}
