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
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;

//
//@AutoConfigureWebTestClient(timeout = "36000")
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class AuthenticationTest extends TestConfig{
//
//    @Value("${authentication.auth.accessTokenCookieName}")
//    private String accessTokenCookieName;
//
//    @Value("${account.admin.username}")
//    private String adminName;
//
//    @Value("${account.admin.password}")
//    private String adminPass;
//
//    @LocalServerPort
//    private int port;
//
//    @Autowired
//    private WebTestClient client;
//
//    @Test
//    public void AuthenticationTest() throws NoSuchAlgorithmException {
//
//        String pass = Util.getInstance().HashPassword(adminPass);
//
//        byte[] token = client.post().uri("/auth/signin")
//                .body(BodyInserters.fromValue(
//                        AuthenticationRequest
//                                .builder()
//                                .username(adminName)
//                                .password(pass)
//                                .build())
//                )
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBody()
//                .jsonPath("token").exists()
//                .returnResult().getResponseBody();
//
//        String authToken = "";
//        try {
//            assert token != null;
//            final ObjectNode node = new ObjectMapper().readValue(new String(token), ObjectNode.class);
//            authToken = node.get("token").toString().replace("\"","");
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        String finalAuthToken = authToken;
//
//        System.out.println("Name: "+accessTokenCookieName + ", value: "+ finalAuthToken);
//
//        client.post().uri("/auth/signin")
//                .body(BodyInserters.fromValue(
//                        AuthenticationRequest
//                                .builder()
//                                .username(adminName)
//                                .password(adminName)
//                                .build())
//                )
//                .exchange()
//                .expectStatus()
//                .isUnauthorized();
//
//        client.get().uri("/auth/me").cookies(cookies -> cookies.add(accessTokenCookieName, finalAuthToken))
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBody()
//                .jsonPath("name").isEqualTo(adminName);
//
//        client.get().uri("/auth/me").cookies(cookies -> cookies.add(accessTokenCookieName, "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2dW9uZyIsInJvbGVzIjoiUk9MRV9BRE1JTixST0xFX1VTRVIiLCJpYXQiOjE2MzM1MjU5NDksImV4cCI6MTYzMzUyOTU0OX0.DCcMxsHsXjyKxIKfX_Smbi-0zQGHz-sB-W7zYuQw9bo"))
//                .exchange()
//                .expectStatus()
//                .is5xxServerError();
//
//        client.get().uri("/auth/me").cookies(cookies -> cookies.add(accessTokenCookieName, "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiaW5oZGllcHF1aW4iLCJyb2xlcyI6IlJPTEVfQURNSU4sUk9MRV9VU0VSIiwiaWF0IjoxNjMzNTM3NDA2LCJleHAiOjE2MzM1NDEwMDZ9.r2RePWB7qNap_AiGxgdpWKjK0K3N57gTJXSxgb6jAjo"))
//                .exchange()
//                .expectStatus()
//                .is5xxServerError();
//
//        client.post().uri("/auth/refresh").cookies(cookies -> cookies.add(accessTokenCookieName, finalAuthToken))
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBody()
//                .jsonPath("token").exists();
//
//        client.post().uri("/auth/admin/account/create").cookies(cookies -> cookies.add(accessTokenCookieName, finalAuthToken))
//                .body(BodyInserters.fromValue(
//                        UpdatePassword
//                                .builder()
//                                .username("quin")
//                                .newpassword(Util.getInstance().HashPassword("quin123"))
//                                .Roles(Arrays.asList("ROLE_ADMIN", "ROLE_USER"))
//                                .build())
//                )
//                .exchange()
//                .expectStatus()
//                .isOk();
//
//        client.post().uri("/auth/admin/account/create").cookies(cookies -> cookies.add(accessTokenCookieName, finalAuthToken))
//                .body(BodyInserters.fromValue(
//                        UpdatePassword
//                                .builder()
//                                .username("nhanvien")
//                                .newpassword(Util.getInstance().HashPassword("nhanvien123"))
//                                .Roles(Collections.singletonList("ROLE_USER"))
//                                .build())
//                )
//                .exchange()
//                .expectStatus()
//                .isOk();
//
//        client.post().uri("/auth/admin/account/create").cookies(cookies -> cookies.add(accessTokenCookieName, finalAuthToken))
//                .body(BodyInserters.fromValue(
//                        UpdatePassword
//                                .builder()
//                                .username("binh")
//                                .newpassword(Util.getInstance().HashPassword("quin123"))
//                                .build())
//                )
//                .exchange()
//                .expectStatus()
//                .is5xxServerError();
//
//        client.post().uri("/auth/admin/account/create").cookies(cookies -> cookies.add(accessTokenCookieName, finalAuthToken))
//                .body(BodyInserters.fromValue(
//                        UpdatePassword
//                                .builder()
//                                .username("binh")
//                                .newpassword(Util.getInstance().HashPassword("binh123"))
//                                .Roles(Arrays.asList("ROLE_ADMIN"))
//                                .build())
//                )
//                .exchange()
//                .expectStatus()
//                .isOk();
//
//        client.post().uri("/auth/admin/account/delete").cookies(cookies -> cookies.add(accessTokenCookieName, finalAuthToken))
//                .body(BodyInserters.fromValue(
//                        UpdatePassword
//                                .builder()
//                                .username("binh")
//                                .oldpassword(Util.getInstance().HashPassword("binh123"))
//                                .Roles(Arrays.asList("ROLE_ADMIN"))
//                                .build())
//                )
//                .exchange()
//                .expectStatus()
//                .isUnauthorized();
//
//        token = client.post().uri("/auth/signin")
//                .body(BodyInserters.fromValue(
//                        AuthenticationRequest
//                                .builder()
//                                .username("binh")
//                                .password(Util.getInstance().HashPassword("binh123"))
//                                .build())
//                )
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBody()
//                .jsonPath("token").exists()
//                .returnResult().getResponseBody();
//
//        try {
//            assert token != null;
//            final ObjectNode node = new ObjectMapper().readValue(new String(token), ObjectNode.class);
//            authToken = node.get("token").toString().replace("\"","");
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("Name: "+accessTokenCookieName + ", value: "+ authToken);
//
//        String finalAuthToken1 = authToken;
//
//        client.post().uri("/auth/admin/account/delete").cookies(cookies -> cookies.add(accessTokenCookieName, finalAuthToken1))
//                .body(BodyInserters.fromValue(
//                        UpdatePassword
//                                .builder()
//                                .username("binh")
//                                .oldpassword(Util.getInstance().HashPassword("binh123"))
//                                .Roles(Arrays.asList("ROLE_ADMIN"))
//                                .build())
//                )
//                .exchange()
//                .expectStatus()
//                .isOk();
//
//        client.post().uri("/auth/signin")
//                .body(BodyInserters.fromValue(
//                        AuthenticationRequest
//                                .builder()
//                                .username("binh")
//                                .password(Util.getInstance().HashPassword("binh123"))
//                                .build())
//                )
//                .exchange()
//                .expectStatus()
//                .isUnauthorized();
//
//        token = client.post().uri("/auth/signin")
//                .body(BodyInserters.fromValue(
//                        AuthenticationRequest
//                                .builder()
//                                .username("quin")
//                                .password(Util.getInstance().HashPassword("quin123"))
//                                .build())
//                )
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBody()
//                .jsonPath("token").exists()
//                .returnResult().getResponseBody();
//
//        try {
//            assert token != null;
//            final ObjectNode node = new ObjectMapper().readValue(new String(token), ObjectNode.class);
//            authToken = node.get("token").toString().replace("\"","");
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("Name: "+accessTokenCookieName + ", value: "+ authToken);
//
//        String finalAuthToken2 = authToken;
//        client.post().uri("/auth/account/update").cookies(cookies -> cookies.add(accessTokenCookieName, finalAuthToken2))
//                .body(BodyInserters.fromValue(
//                        UpdatePassword
//                                .builder()
//                                .username("quin")
//                                .oldpassword(Util.getInstance().HashPassword("quin123"))
//                                .newpassword(Util.getInstance().HashPassword("qqqqq123"))
//                                .Roles(Arrays.asList("ROLE_ADMIN"))
//                                .build())
//                )
//                .exchange()
//                .expectStatus()
//                .isOk();
//
//        client.post().uri("/auth/signin")
//                .body(BodyInserters.fromValue(
//                        AuthenticationRequest
//                                .builder()
//                                .username("quin")
//                                .password(Util.getInstance().HashPassword("quin123"))
//                                .build())
//                )
//                .exchange()
//                .expectStatus()
//                .isUnauthorized();
//
//        client.post().uri("/auth/signin")
//                .body(BodyInserters.fromValue(
//                        AuthenticationRequest
//                                .builder()
//                                .username("quin")
//                                .password(Util.getInstance().HashPassword("qqqqq123"))
//                                .build())
//                )
//                .exchange()
//                .expectStatus()
//                .isOk();
//
//        token = client.post().uri("/auth/signin")
//                .body(BodyInserters.fromValue(
//                        AuthenticationRequest
//                                .builder()
//                                .username("nhanvien")
//                                .password(Util.getInstance().HashPassword("nhanvien123"))
//                                .build())
//                )
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBody()
//                .jsonPath("token").exists()
//                .returnResult().getResponseBody();
//
//        try {
//            assert token != null;
//            final ObjectNode node = new ObjectMapper().readValue(new String(token), ObjectNode.class);
//            authToken = node.get("token").toString().replace("\"","");
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("Name: "+accessTokenCookieName + ", value: "+ authToken);
//
//        String finalAuthToken3 = authToken;
//        client.post().uri("/auth/account/update").cookies(cookies -> cookies.add(accessTokenCookieName, finalAuthToken3))
//                .body(BodyInserters.fromValue(
//                        UpdatePassword
//                                .builder()
//                                .username("nhanvien")
//                                .oldpassword(Util.getInstance().HashPassword("nhanvien123"))
//                                .newpassword(Util.getInstance().HashPassword("nhanvien"))
//                                .Roles(Arrays.asList("ROLE_ADMIN"))
//                                .build())
//                )
//                .exchange()
//                .expectStatus()
//                .isOk();
//
//        client.post().uri("/auth/signin")
//                .body(BodyInserters.fromValue(
//                        AuthenticationRequest
//                                .builder()
//                                .username("nhanvien")
//                                .password(Util.getInstance().HashPassword("nhanvien123"))
//                                .build())
//                )
//                .exchange()
//                .expectStatus()
//                .isUnauthorized();
//
//        client.post().uri("/auth/signin")
//                .body(BodyInserters.fromValue(
//                        AuthenticationRequest
//                                .builder()
//                                .username("nhanvien")
//                                .password(Util.getInstance().HashPassword("nhanvien"))
//                                .build())
//                )
//                .exchange()
//                .expectStatus()
//                .isOk();
//
//        client.post().uri("/auth/admin/account/create").cookies(cookies -> cookies.add(accessTokenCookieName, finalAuthToken3))
//                .body(BodyInserters.fromValue(
//                        UpdatePassword
//                                .builder()
//                                .username("nhanvien1")
//                                .newpassword(Util.getInstance().HashPassword("nhanvien111"))
//                                .Roles(Arrays.asList("ROLE_ADMIN"))
//                                .build())
//                )
//                .exchange()
//                .expectStatus()
//                .isForbidden();
//    }
//
//    @Test
//    public void uploadImage() throws NoSuchAlgorithmException {
//
//        String pass = Util.getInstance().HashPassword(adminPass);
//
//        byte[] token = client.post().uri("/auth/signin")
//                .body(BodyInserters.fromValue(
//                        AuthenticationRequest
//                                .builder()
//                                .username(adminName)
//                                .password(pass)
//                                .build())
//                )
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBody()
//                .jsonPath("token").exists()
//                .returnResult().getResponseBody();
//
//        String authToken = "";
//        try {
//            assert token != null;
//            final ObjectNode node = new ObjectMapper().readValue(new String(token), ObjectNode.class);
//            authToken = node.get("token").toString().replace("\"","");
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        String finalAuthToken = authToken;
//
//        System.out.println("Name: "+accessTokenCookieName + ", value: "+ finalAuthToken);
//
//        MultipartBodyBuilder builder = new MultipartBodyBuilder();
//        builder.part("file", new FileSystemResource(new File("C:\\Users\\phong\\Downloads\\top_icon.png")));
//
//
//        client.post().uri("/carousel/admin/upload").cookies(cookies -> cookies.add(accessTokenCookieName, finalAuthToken))
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .body(
//                        BodyInserters.fromMultipartData(builder.build())
//                )
//                .exchange()
//                .expectStatus()
//                .isOk();
//    }
//}

public class AuthenticationTest{}