package com.example.heroku.startup;

import com.example.heroku.model.Users;
import com.example.heroku.model.repository.UserRepository;
import com.example.heroku.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static java.util.Arrays.asList;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer {

    @Autowired
    DatabaseClient databaseClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @EventListener(value = ApplicationReadyEvent.class)
    public void init() throws NoSuchAlgorithmException {
        System.out.println("initializing vehicles data...");
        log.debug("initializing vehicles data...");


        asList(
                //"DROP TABLE beer",
                "DROP TABLE image",
                "DROP TABLE users",
                "CREATE TABLE IF NOT EXISTS image (Id SERIAL PRIMARY KEY, Content bytea, Category VARCHAR, Createat TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS users (Id SERIAL PRIMARY KEY, Username VARCHAR, Password VARCHAR, Active BOOL, Roles VARCHAR, Createat TIMESTAMP)")
                .forEach(s ->
                        databaseClient.execute(s)
                                .fetch()
                                .rowsUpdated()
                                .block()
                );


        String pass = this.passwordEncoder.encode(Util.getInstance().HashPassword("hoduongvuong123"));
        System.out.println(pass);

        var initPosts = this.userRepository.deleteAll()
                .thenMany(
                        Mono.just("vuong")
                                .flatMap(username -> {
                                    Users users = Users.builder()
                                            .username(username)
                                            .password(pass)
                                            .roles(Arrays.asList("ROLE_ADMIN", "ROLE_USER"))
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

        System.out.println("Done initializing vehicles data...");
        log.debug("Done initializing vehicles data...");
    }
}
