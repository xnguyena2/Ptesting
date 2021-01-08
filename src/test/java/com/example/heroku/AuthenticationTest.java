package com.example.heroku;

import com.example.heroku.request.data.AuthenticationRequest;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

/*
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "JDBC_DATABASE_URL=r2dbc:postgresql://ec2-52-203-165-126.compute-1.amazonaws.com:5432/dab63e0a6snl77",
        "POSTGRESQL_PORT=5432",
        "POSTGRESQL_DB=dab63e0a6snl77",
        "POSTGRESQL_HOST=ec2-52-203-165-126.compute-1.amazonaws.com",
        "USER_NAME=pnkrlyomnugskb",
        "PASSWORD=4b16a191f30c89f36908193809d12d3a8d4c3ffb73277357e1b0e0515169cea2",
        "POSTGRESQL_POOLSIZE=1",
        "POSTGRESQL_TIMEIDLE=10000",
        "HTTPONLY_SECURE=false"})
public class AuthenticationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient client;

    @Test
    public void AuthenticationTest() {

        client.post().uri("/auth/signin")
                .body(BodyInserters.fromValue(
                        AuthenticationRequest
                                .builder()
                                .username("vuong")
                                .password("102352E65CA56C1F07A68A2FA142CE57")
                                .build())
                )
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("token").exists();

        client.post().uri("/auth/signin")
                .body(BodyInserters.fromValue(
                        AuthenticationRequest
                                .builder()
                                .username("vuong")
                                .password("vuong")
                                .build())
                )
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }
}
*/


public class AuthenticationTest {}