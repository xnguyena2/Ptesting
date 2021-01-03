package com.example.heroku.startup;

import com.example.heroku.model.Beer;
import com.example.heroku.model.BeerUnit;
import com.example.heroku.model.Users;
import com.example.heroku.model.repository.BeerRepository;
import com.example.heroku.model.repository.UserRepository;
import com.example.heroku.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

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

    @Autowired
    BeerRepository beerRepository;

    @EventListener(value = ApplicationReadyEvent.class)
    public void init() throws NoSuchAlgorithmException {
        System.out.println("initializing vehicles data...");
        log.debug("initializing vehicles data...");


        asList(
                "DROP TABLE IF EXISTS ticks",
                "DROP TABLE IF EXISTS image",
                "DROP TABLE IF EXISTS users",
                "DROP TABLE IF EXISTS beer",
                "DROP TABLE IF EXISTS beer_unit",
                "DROP TABLE IF EXISTS search_token",
                "DROP TABLE IF EXISTS device_config",
                "CREATE TABLE IF NOT EXISTS search_token (id SERIAL PRIMARY KEY, beer_second_id VARCHAR, tokens TSVECTOR)",
                "CREATE TABLE IF NOT EXISTS beer (id SERIAL PRIMARY KEY, beer_second_id VARCHAR, name VARCHAR, detail TEXT, category VARCHAR, meta_search TEXT, createat TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS beer_unit (id SERIAL PRIMARY KEY, beer VARCHAR, name VARCHAR, price float8, discount float8, date_expire TIMESTAMP, ship_price VARCHAR, createat TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS image (id SERIAL PRIMARY KEY, content bytea, category VARCHAR, createat TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY, username VARCHAR, password VARCHAR, active BOOL, roles VARCHAR, createat TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS device_config (id SERIAL PRIMARY KEY, color VARCHAR)"
        )
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
/*
        var beerpost = this.beerRepository.deleteAll()
                .thenMany(
                        Flux.just("beer1", "beer2")
                        .flatMap(
                                beerName ->{
                                    Beer beer = Beer.builder()
                                            .name(beerName)
                                            .category(Beer.Category.ALCOHOL)
                                            .build();
                                    return this.beerRepository.save(beer);
                                }
                        )
                );
        beerpost.doOnSubscribe(data -> log.info("data:" + data))
                .subscribe(
                        data -> log.info("data:" + data), err -> log.error("error:" + err),
                        () -> log.info("done initialization...")
                );
*/
        System.out.println("Done initializing vehicles data...");
        log.debug("Done initializing vehicles data...");
    }
}
