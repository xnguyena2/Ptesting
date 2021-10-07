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

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

import static java.util.Arrays.asList;

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
        System.out.println("initializing vehicles data...");
        log.debug("initializing vehicles data...");

        if (resetDB) {
            log.warn("Reset all Database!");
            System.out.println("Reset all Database!");

            imageAPI.DeleteAll().block();

            asList(

                    "DROP TABLE IF EXISTS ticks",
                    "DROP TABLE IF EXISTS image",
                    "DROP TABLE IF EXISTS users",
                    "DROP TABLE IF EXISTS beer",
                    "DROP TABLE IF EXISTS beer_unit",
                    "DROP TABLE IF EXISTS search_token",
                    "DROP TABLE IF EXISTS device_config",
                    "DROP TABLE IF EXISTS package_order",
                    "DROP TABLE IF EXISTS beer_order",
                    "DROP TABLE IF EXISTS beer_unit_order",
                    "DROP TABLE IF EXISTS user_device",
                    "DROP TABLE IF EXISTS user_address",
                    "DROP TABLE IF EXISTS user_package",
                    "DROP TABLE IF EXISTS voucher",
                    "DROP TABLE IF EXISTS voucher_relate_user_device",
                    "DROP TABLE IF EXISTS voucher_relate_beer",
                    "DROP TABLE IF EXISTS beer_view_count",
                    "DROP TABLE IF EXISTS notification",
                    "DROP TABLE IF EXISTS notification_relate_user_device",
                    "DROP TABLE IF EXISTS shipping_provider"
            )
                    .forEach(s ->
                            databaseClient.execute(s)
                                    .fetch()
                                    .rowsUpdated()
                                    .block()
                    );
            //PhotoLib.getInstance().deleteAll();
            flickrLib.DeleteAll();

        } else {
            log.info("No Need reset Database@");
            System.out.println("No Need reset Database@");
        }

        asList(
                "CREATE TABLE IF NOT EXISTS search_token (id SERIAL PRIMARY KEY, beer_second_id VARCHAR, tokens TSVECTOR)",
                "CREATE TABLE IF NOT EXISTS beer (id SERIAL PRIMARY KEY, beer_second_id VARCHAR, name VARCHAR, detail TEXT, category VARCHAR, meta_search TEXT, status VARCHAR, createat TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS beer_unit (id SERIAL PRIMARY KEY, beer_unit_second_id VARCHAR, beer VARCHAR, name VARCHAR, price float8, discount float8, date_expire TIMESTAMP, volumetric float8, weight float8, createat TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS image (id SERIAL PRIMARY KEY, imgid VARCHAR, thumbnail VARCHAR, medium VARCHAR, large VARCHAR, category VARCHAR, createat TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY, username VARCHAR, password VARCHAR, active BOOL, roles VARCHAR, createat TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS device_config (id SERIAL PRIMARY KEY, color VARCHAR)",

                "CREATE TABLE IF NOT EXISTS package_order (id SERIAL PRIMARY KEY, package_order_second_id VARCHAR, user_device_id VARCHAR, reciver_address VARCHAR, region_id INTEGER, district_id INTEGER, ward_id INTEGER, reciver_fullname VARCHAR, phone_number VARCHAR, total_price float8, ship_price float8, status VARCHAR, createat TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS beer_order (id SERIAL PRIMARY KEY, name VARCHAR, package_order_second_id VARCHAR, beer_second_id VARCHAR, voucher_second_id VARCHAR, total_price float8, ship_price float8, createat TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS beer_unit_order (id SERIAL PRIMARY KEY, name VARCHAR, package_order_second_id VARCHAR, beer_second_id VARCHAR, beer_unit_second_id VARCHAR, price float8, total_discount float8, number_unit INTEGER, createat TIMESTAMP)",

                "CREATE TABLE IF NOT EXISTS user_device (id SERIAL PRIMARY KEY, device_id VARCHAR, user_first_name VARCHAR, user_last_name VARCHAR, status VARCHAR, createat TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS user_address (id SERIAL PRIMARY KEY, device_id VARCHAR, reciver_fullname VARCHAR, phone_number VARCHAR, house_number VARCHAR, region INTEGER, district INTEGER, ward INTEGER, status VARCHAR, createat TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS user_package (id SERIAL PRIMARY KEY, device_id VARCHAR, beer_id VARCHAR, beer_unit VARCHAR, number_unit INTEGER, status VARCHAR, createat TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS voucher (id SERIAL PRIMARY KEY, voucher_second_id VARCHAR, detail VARCHAR, discount float8, amount float8, reuse INTEGER, for_all_beer BOOLEAN, for_all_user BOOLEAN, date_expire TIMESTAMP, status VARCHAR, createat TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS voucher_relate_user_device (id SERIAL PRIMARY KEY, voucher_second_id VARCHAR, reuse INTEGER, device_id VARCHAR, createat TIMESTAMP, UNIQUE (voucher_second_id, device_id))",
                "CREATE TABLE IF NOT EXISTS voucher_relate_beer (id SERIAL PRIMARY KEY, voucher_second_id VARCHAR, beer_second_id VARCHAR, createat TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS beer_view_count (id SERIAL PRIMARY KEY, beer_id VARCHAR, device_id VARCHAR, createat TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS notification (id SERIAL PRIMARY KEY, notification_second_id VARCHAR, title VARCHAR, detail VARCHAR, createat TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS notification_relate_user_device (id SERIAL PRIMARY KEY, notification_second_id VARCHAR, user_device_id VARCHAR, createat TIMESTAMP, status VARCHAR)",
                "CREATE TABLE IF NOT EXISTS shipping_provider (id SERIAL PRIMARY KEY, provider_id VARCHAR, name VARCHAR, config TEXT, createat TIMESTAMP)"
        )
                .forEach(s ->
                        databaseClient.execute(s)
                                .fetch()
                                .rowsUpdated()
                                .block()
                );


        if (resetDB) {
            String pass = this.passwordEncoder.encode(Util.getInstance().HashPassword(adminPass));
            //System.out.println(pass);

            var initPosts = this.userRepository.deleteAll()
                    .thenMany(
                            Mono.just(adminName)
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
        }

        System.out.println("Done initializing vehicles data...");
        log.debug("Done initializing vehicles data...");
    }
}
