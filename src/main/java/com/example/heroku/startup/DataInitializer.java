package com.example.heroku.startup;

import com.example.heroku.model.Users;
import com.example.heroku.model.repository.UserRepository;
import com.example.heroku.photo.FlickrLib;
import com.example.heroku.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;

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
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private FlickrLib flickrLib;

    @EventListener(value = ApplicationReadyEvent.class)
    public void init() throws NoSuchAlgorithmException {
        System.out.println("initializing database...");
        log.debug("initializing database...");

        if (resetDB) {
            log.warn("Reset all Database!");
            System.out.println("Reset all Database!");

            imageAPI.DeleteAll().subscribe();

            //PhotoLib.getInstance().deleteAll();
            flickrLib.DeleteAll();

            try {
                String dropSQL = new String(Files.readAllBytes(Paths.get("Asset/drop.sql")));
                databaseClient.execute(dropSQL)
                        .fetch()
                        .rowsUpdated()
                        .subscribe();

                dropSQL = new String(Files.readAllBytes(Paths.get("Asset/database.sql")));
                databaseClient.execute(dropSQL)
                        .fetch()
                        .rowsUpdated()
                        .subscribe();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String pass = this.passwordEncoder.encode(Util.getInstance().HashPassword(adminPass));
            //System.out.println(pass);

            var initPosts = this.userRepository.deleteAll()
                    .thenMany(
                            Mono.just(adminName)
                                    .flatMap(username -> {
                                        Users users = Users.builder()
                                                .group_id(adminStore)
                                                .username(username)
                                                .password(pass)
                                                .roles(Collections.singletonList(Util.ROLE.ROLE_ROOT.getName()))
                                                .createat(new Timestamp(new Date().getTime()))
                                                .active(true)
                                                .build();
                                        return this.userRepository.save(users);
                                    })
                    );


            initPosts.doOnSubscribe(data -> log.info("data:" + data))
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
