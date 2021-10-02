package com.example.heroku;
/*
import com.example.heroku.request.data.AuthenticationRequest;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.io.File;


public class AuthenticationTest extends TestConfig{
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

    @Test
    public void uploadImage(){
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", new FileSystemResource(new File("C:\\Users\\phong\\Downloads\\top_icon.png")));


        client.post().uri("/carousel/upload")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(
                        BodyInserters.fromMultipartData(builder.build())
                )
                .exchange()
                .expectStatus()
                .isOk();
    }
}
*/

public class AuthenticationTest {}