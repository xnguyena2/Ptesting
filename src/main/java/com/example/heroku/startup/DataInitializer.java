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

        /*
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 1");

            //stmt.executeUpdate("DROP TABLE IMAGE");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS IMAGE (ImgID CHAR(50) PRIMARY KEY, Content TEXT, Category CHAR(50), CreateAT TIMESTAMP)");

            //stmt.executeUpdate("DROP TABLE USERS");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS USERS (ID BIGINT PRIMARY KEY, Username CHAR(50), Password CHAR(100), Roles CHAR(100))");

            //stmt.executeUpdate("DROP TABLE BEER");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS BEER (ID CHAR(50) PRIMARY KEY, Name CHAR(250), Detail TEXT, Discount FLOAT, DícountEX TIMESTAMP, CreateAT TIMESTAMP)");

            String pass = this.passwordEncoder.encode(Util.getInstance().HashPassword("hoduongvuong123"));
            System.out.println(pass);


            this.userRepository.save(User.builder()
                    .username("vuong")
                    .password(pass)
                    .roles(Arrays.asList("ROLE_ADMIN", "ROLE_USER"))
                    .build()
            );


        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        */
        System.out.println("Done initializing vehicles data...");
        log.debug("Done initializing vehicles data...");
    }
}
