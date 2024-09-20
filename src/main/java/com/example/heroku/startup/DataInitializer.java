package com.example.heroku.startup;

import com.example.heroku.request.data.UpdatePassword;
import com.example.heroku.request.store.StoreInitData;
import com.example.heroku.services.Store;
import com.example.heroku.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer {

    @Value("${app.setting.resetdb}")
    private boolean resetDB;

    @Value("${account.admin.username}")
    private String adminName;

    @Value("${account.admin.password}")
    private String adminPass;

    @Value("${account.admin.store}")
    private String adminStore;

    @Autowired
    com.example.heroku.services.Image imageAPI;

    @Autowired
    DatabaseClient databaseClient;

    @Autowired
    private Store storeServices;

    @Autowired
    PasswordEncoder passwordEncoder;

    @EventListener(value = ApplicationReadyEvent.class)
    public void init() throws NoSuchAlgorithmException {
        System.out.println("initializing database...");
        log.debug("initializing database...");

        if (resetDB) {
            log.warn("Reset all Database!");
            System.out.println("Reset all Database!");

            imageAPI.DeleteAll().subscribe();

            //PhotoLib.getInstance().deleteAll();

            try {
                String dropSQL = new String(Files.readAllBytes(Paths.get("Asset/drop.sql")));
                databaseClient.sql(dropSQL)
                        .fetch()
                        .rowsUpdated()
                        .subscribe();

                dropSQL = new String(Files.readAllBytes(Paths.get("Asset/database.sql")));
                databaseClient.sql(dropSQL)
                        .fetch()
                        .rowsUpdated()
                        .subscribe();

                //wait sometime for database initial
                Thread.sleep(10000);

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            this.storeServices.initialStoreWithoutCheckRole(StoreInitData.builder()
                            .newAccount(UpdatePassword.builder()
                                    .roles(Collections.singletonList(Util.ROLE.ROLE_ROOT.getName()))
                                    .group_id(adminStore)
                                    .username(adminName)
                                    .newpassword(Util.getInstance().HashPassword(adminPass))
                                    .build())
                            .store(com.example.heroku.model.Store.builder()
                                    .phone("121212")
                                    .address("123 abc")
                                    .name("shop ban chuoi")
                                    .time_open("all time")
                                    .build())
                            .build())
                    .doOnSubscribe(data -> log.info("data:" + data))
                    .subscribe(
                            data -> log.info("data:" + data), err -> log.error("error:" + err),
                            () -> log.info("done initialization...")
                    );

        } else {
            log.info("No Need reset Database@");
            System.out.println("No Need reset Database@");
        }

        System.out.println("Done initializing database...");
        log.debug("Done initializing database...");
    }
}
