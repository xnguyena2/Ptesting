package com.example.heroku.startup;

import com.example.heroku.model.User;
import com.example.heroku.model.repository.UserRepository;
import com.example.heroku.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Arrays;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DataSource dataSource;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("initializing vehicles data...");
        log.debug("initializing vehicles data...");
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 1");

            //stmt.executeUpdate("DROP TABLE IMAGE");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS IMAGE (ImgID CHAR(50) PRIMARY KEY, Content TEXT, Category CHAR(50), CreateAt TIMESTAMP)");

            //stmt.executeUpdate("DROP TABLE USERS");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS USERS (ID BIGINT PRIMARY KEY, Username CHAR(50), Password CHAR(100), Roles CHAR(100))");

            /*
            String pass = this.passwordEncoder.encode("hoduongvuong123");
            System.out.println(pass);


            this.userRepository.save(User.builder()
                    .username("vuong")
                    .password(pass)
                    .roles(Arrays.asList("ROLE_ADMIN", "ROLE_USER"))
                    .build()
            );
*/

        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        System.out.println("Done initializing vehicles data...");
        log.debug("Done initializing vehicles data...");
    }
}
