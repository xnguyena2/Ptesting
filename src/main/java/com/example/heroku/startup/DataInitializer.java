package com.example.heroku.startup;

import com.example.heroku.model.Users;
import com.example.heroku.model.repository.UserRepository;
import com.example.heroku.services.ShippingProvider;
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
import reactor.core.publisher.Mono;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

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
                "DROP TABLE IF EXISTS ticks",
                "DROP TABLE IF EXISTS image",
                "DROP TABLE IF EXISTS users",
                "DROP TABLE IF EXISTS beer",
                "DROP TABLE IF EXISTS beer_unit",
                "DROP TABLE IF EXISTS search_token",
                "DROP TABLE IF EXISTS device_config",
                "DROP TABLE IF EXISTS beer_order",
                "DROP TABLE IF EXISTS user_device",
                "DROP TABLE IF EXISTS user_address",
                "DROP TABLE IF EXISTS user_package",
                "DROP TABLE IF EXISTS voucher",
                "DROP TABLE IF EXISTS voucher_relate_user_device",
                "DROP TABLE IF EXISTS voucher_relate_beer",
                "DROP TABLE IF EXISTS beer_view_count",
                "DROP TABLE IF EXISTS notification",
                "DROP TABLE IF EXISTS notification_relate_user_device",
                "DROP TABLE IF EXISTS shipping_provider",
                "CREATE TABLE IF NOT EXISTS search_token (id SERIAL PRIMARY KEY, beer_second_id VARCHAR, tokens TSVECTOR)",
                "CREATE TABLE IF NOT EXISTS beer (id SERIAL PRIMARY KEY, beer_second_id VARCHAR, name VARCHAR, detail TEXT, category VARCHAR, meta_search TEXT, status VARCHAR, createat TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS beer_unit (id SERIAL PRIMARY KEY, beer_unit_second_id VARCHAR, beer VARCHAR, name VARCHAR, price float8, discount float8, date_expire TIMESTAMP, volumetric float8, weight float8, createat TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS image (id SERIAL PRIMARY KEY, content bytea, category VARCHAR, createat TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY, username VARCHAR, password VARCHAR, active BOOL, roles VARCHAR, createat TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS device_config (id SERIAL PRIMARY KEY, color VARCHAR)",
                "CREATE TABLE IF NOT EXISTS beer_order (id SERIAL PRIMARY KEY, beer_id VARCHAR, beer_unit INTEGER, user_id VARCHAR, voucher_second_id VARCHAR, reciver_address_id INTEGER, total_price float8, ship_price float8, number_unit INTEGER, status VARCHAR, createat TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS user_device (id SERIAL PRIMARY KEY, device_id VARCHAR, user_first_name VARCHAR, user_last_name VARCHAR, status VARCHAR, createat TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS user_address (id SERIAL PRIMARY KEY, device_id VARCHAR, reciver_fullname VARCHAR, phone_number VARCHAR, house_number VARCHAR, region INTEGER, district INTEGER, ward INTEGER, status VARCHAR, createat TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS user_package (id SERIAL PRIMARY KEY, device_id VARCHAR, beer_id VARCHAR, beer_unit INTEGER, number_unit INTEGER, status VARCHAR, createat TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS voucher (id SERIAL PRIMARY KEY, voucher_second_id VARCHAR, detail VARCHAR, discount float8, amount float8, reuse INTEGER, for_all_beer BOOLEAN, for_all_user BOOLEAN, date_expire TIMESTAMP, status VARCHAR, createat TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS voucher_relate_user_device (id SERIAL PRIMARY KEY, voucher_second_id VARCHAR, reuse INTEGER, device_id VARCHAR, createat TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS voucher_relate_beer (id SERIAL PRIMARY KEY, voucher_second_id VARCHAR, beer_second_id VARCHAR, createat TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS beer_view_count (id SERIAL PRIMARY KEY, beer_id VARCHAR, device_id VARCHAR, createat TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS notification (id SERIAL PRIMARY KEY, title VARCHAR, detail VARCHAR, createat TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS notification_relate_user_device (id SERIAL PRIMARY KEY, notification_id INTEGER, user_device VARCHAR, createat TIMESTAMP, status VARCHAR)",
                "CREATE TABLE IF NOT EXISTS shipping_provider (id SERIAL PRIMARY KEY, provider_id VARCHAR, name VARCHAR, config TEXT, createat TIMESTAMP)"
        )
                .forEach(s ->
                        databaseClient.execute(s)
                                .fetch()
                                .rowsUpdated()
                                .block()
                );


        String pass = this.passwordEncoder.encode(Util.getInstance().HashPassword("hoduongvuong123"));
        //System.out.println(pass);

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



        System.out.println("Done initializing vehicles data...");
        log.debug("Done initializing vehicles data...");
    }
}
