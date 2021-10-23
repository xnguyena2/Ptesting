package com.example.heroku;

import com.example.heroku.request.data.AuthenticationRequest;
import com.example.heroku.request.data.UpdatePassword;
import com.example.heroku.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;

/*
@AutoConfigureWebTestClient(timeout = "36000")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationTest extends TestConfig{

    @Value("${authentication.auth.accessTokenCookieName}")
    private String accessTokenCookieName;

    @Value("${account.admin.username}")
    private String adminName;

    @Value("${account.admin.password}")
    private String adminPass;

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient client;

    String getToken(String username, String password) throws NoSuchAlgorithmException {

        byte[] token = client.post().uri("/auth/signin")
                .body(BodyInserters.fromValue(
                        AuthenticationRequest
                                .builder()
                                .username(username)
                                .password(Util.getInstance().HashPassword(password))
                                .build())
                )
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("token").exists()
                .returnResult().getResponseBody();

        String authToken = "";
        try {
            assert token != null;
            final ObjectNode node = new ObjectMapper().readValue(new String(token), ObjectNode.class);
            authToken = node.get("token").toString().replace("\"","");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println("Name: "+accessTokenCookieName + ", value: "+ authToken);
        return authToken;
    }

    StatusAssertions deleteAcc(String username, String password, String token, boolean isAdmin) throws NoSuchAlgorithmException {

        String path = "/auth/admin/account/delete";
        if(!isAdmin){
            path = "/auth/account/delete";
        }

        return client.post().uri(path).cookies(cookies -> cookies.add(accessTokenCookieName, token))
                .body(BodyInserters.fromValue(
                        UpdatePassword
                                .builder()
                                .username(username)
                                .oldpassword(Util.getInstance().HashPassword(password))
                                .build())
                )
                .exchange()
                .expectStatus();
    }

    StatusAssertions createAcc(String username, String password, String token, List<String> roles) throws NoSuchAlgorithmException {

        return client.post().uri("/auth/admin/account/create").cookies(cookies -> cookies.add(accessTokenCookieName, token))
                .body(BodyInserters.fromValue(
                        UpdatePassword
                                .builder()
                                .username(username)
                                .newpassword(Util.getInstance().HashPassword(password))
                                .roles(roles)
                                .build())
                )
                .exchange()
                .expectStatus();
    }

    StatusAssertions signinAcc(String username, String password) throws NoSuchAlgorithmException {

        return client.post().uri("/auth/signin")
                .body(BodyInserters.fromValue(
                        AuthenticationRequest
                                .builder()
                                .username(username)
                                .password(Util.getInstance().HashPassword(password))
                                .build())
                )
                .exchange()
                .expectStatus();
    }

    StatusAssertions updateAcc(String username, String oldPass, String newPass, String token) throws NoSuchAlgorithmException {

        return
                client.post().uri("/auth/account/update").cookies(cookies -> cookies.add(accessTokenCookieName, token))
                        .body(BodyInserters.fromValue(
                                UpdatePassword
                                        .builder()
                                        .username(username)
                                        .oldpassword(Util.getInstance().HashPassword(oldPass))
                                        .newpassword(Util.getInstance().HashPassword(newPass))
                                        .roles(Collections.singletonList(Util.ROLE.ROLE_ADMIN.getName()))
                                        .build())
                        )
                        .exchange()
                        .expectStatus();
    }

    @Test
    public void AuthenticationTest() throws NoSuchAlgorithmException {

        String finalAuthToken = getToken(adminName, adminPass);

        signinAcc(adminName, adminName)
                .isUnauthorized();

        client.get().uri("/auth/me").cookies(cookies -> cookies.add(accessTokenCookieName, finalAuthToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("name").isEqualTo(adminName);

        client.get().uri("/auth/me").cookies(cookies -> cookies.add(accessTokenCookieName, "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2dW9uZyIsInJvbGVzIjoiUk9MRV9BRE1JTixST0xFX1VTRVIiLCJpYXQiOjE2MzM1MjU5NDksImV4cCI6MTYzMzUyOTU0OX0.DCcMxsHsXjyKxIKfX_Smbi-0zQGHz-sB-W7zYuQw9bo"))
                .exchange()
                .expectStatus()
                .is5xxServerError();

        client.get().uri("/auth/me").cookies(cookies -> cookies.add(accessTokenCookieName, "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiaW5oZGllcHF1aW4iLCJyb2xlcyI6IlJPTEVfQURNSU4sUk9MRV9VU0VSIiwiaWF0IjoxNjMzNTM3NDA2LCJleHAiOjE2MzM1NDEwMDZ9.r2RePWB7qNap_AiGxgdpWKjK0K3N57gTJXSxgb6jAjo"))
                .exchange()
                .expectStatus()
                .is5xxServerError();

        client.post().uri("/auth/refresh").cookies(cookies -> cookies.add(accessTokenCookieName, finalAuthToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("token").exists();

        createAcc("quin", "quin123", finalAuthToken, Collections.singletonList(Util.ROLE.ROLE_ADMIN.getName()))
                .isOk();


        createAcc("nhanvien", "nhanvien123", finalAuthToken, Collections.singletonList(Util.ROLE.ROLE_USER.getName()))
                .isOk();


        createAcc("binh", "quin123", finalAuthToken, null)
                .is5xxServerError();


        createAcc("binh", "binh123", finalAuthToken, Collections.singletonList(Util.ROLE.ROLE_ADMIN.getName()))
                .isOk();


        deleteAcc("binh", "binh123", finalAuthToken, true)
                .isOk();

        createAcc("binh", "binh123", finalAuthToken, Collections.singletonList(Util.ROLE.ROLE_ADMIN.getName()))
                .isOk();

        String finalAuthToken1 = getToken("binh", "binh123");

        deleteAcc("binh", "binh123", finalAuthToken1, false)
                .isOk();


        signinAcc("binh", "binh123")
                .isUnauthorized();

        String finalAuthToken2 = getToken("quin", "quin123");


        updateAcc("quin", "quin123", "qqqqq123", finalAuthToken2)
                .isOk();

        signinAcc("quin", "quin123")
                .isUnauthorized();

        signinAcc("quin", "qqqqq123")
                .isOk();

        String finalAuthToken3 = getToken("nhanvien", "nhanvien123");


        updateAcc("nhanvien", "nhanvien123", "nhanvien", finalAuthToken3)
                .isOk();


        updateAcc("nhanvien", "nhanvien123", "nhanvien", finalAuthToken3)
                .isUnauthorized();

        signinAcc("nhanvien", "nhanvien123")
                .isUnauthorized();


        signinAcc("nhanvien", "nhanvien")
                .isOk();


        createAcc("nhanvien1", "nhanvien111", finalAuthToken3, Collections.singletonList(Util.ROLE.ROLE_ADMIN.getName()))
                .isForbidden();



        createAcc("nhanvien1", "nhanvien123", finalAuthToken, Collections.singletonList(Util.ROLE.ROLE_USER.getName()))
                .isOk();

        createAcc("nhanvien2", "nhanvien123", finalAuthToken, Collections.singletonList(Util.ROLE.ROLE_USER.getName()))
                .isOk();


        createAcc("nhanvien3", "nhanvien123", finalAuthToken, Collections.singletonList(Util.ROLE.ROLE_USER.getName()))
                .isOk();

        String finalAuthToken4 = getToken("nhanvien2", "nhanvien123");

        deleteAcc("nhanvien3", "nhanvien123", finalAuthToken4, false)
                .isForbidden();

        deleteAcc("quin", "nhanvien123", finalAuthToken4, false)
                .isForbidden();

        deleteAcc("quin", "qqqqq123", finalAuthToken4, false)
                .isUnauthorized();

        deleteAcc("nhanvien2", "nhanvien123", finalAuthToken4, false)
                .isOk();

        deleteAcc("nhanvien1", "qqqqq123", finalAuthToken2, true)
                .isOk();

        deleteAcc("quin", adminPass, finalAuthToken, true)
                .isOk();

        deleteAcc("nhanvien3", adminPass, finalAuthToken, true)
                .isOk();

        deleteAcc("nhanvien", "qqqqq123", finalAuthToken2, false)
                .isUnauthorized();

        deleteAcc("nhanvien", adminPass, finalAuthToken, true)
                .isOk();
    }

    @Test
    public void uploadImage() throws NoSuchAlgorithmException {


        String finalAuthToken = getToken(adminName, adminPass);

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", new FileSystemResource(new File("C:\\Users\\phong\\Downloads\\test.png")));


        client.post().uri("/carousel/admin/upload").cookies(cookies -> cookies.add(accessTokenCookieName, finalAuthToken))
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
public class AuthenticationTest{}