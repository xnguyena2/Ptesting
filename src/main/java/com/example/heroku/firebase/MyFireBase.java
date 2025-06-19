package com.example.heroku.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
@Slf4j
@RequiredArgsConstructor
public class MyFireBase {

    final String registrationTopic = "trumbien";

    @Value("${firebase.admin.credential}")
    private String firebaseCredential;

    @Getter
    private boolean isAuthSuccess = false;

    @Getter
    private Storage storage;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        this.auth();
    }

    private void auth() {
        if (!StringUtils.hasLength(firebaseCredential)) {
            isAuthSuccess = false;
            return;
        }
        try {
            InputStream serviceAccount = new ByteArrayInputStream(firebaseCredential.getBytes());
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
            storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

            isAuthSuccess = true;
            log.debug("initializing firebase success...");

        } catch (IOException ioEx) {
            log.error("Failed to initialize Firebase", ioEx);
        }

    }

    public String SendNotification(String title, String msg, String token) {

        if (!isAuthSuccess) {
            return "not AuthSuccess";
        }

        try {

            Message message = Message.builder()
                    .setNotification(
                            Notification.builder()
                                    .setTitle(title)
                                    .setBody(msg)
                                    .build()
                    )
                    .setAndroidConfig(AndroidConfig.builder()
                            .setPriority(AndroidConfig.Priority.HIGH)
                            .build())
                    .putData("title", title)
                    .putData("body", msg)
                    .setToken(token)
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);

            log.info("Successfully sent FCM message: {}", response);
            return response;
        } catch (Exception ex) {
            log.error("Failed to send FCM message", ex);
            return "Failed to send notification.";
        }
    }

    public void SendNotification2Admin(String title, String msg) {

        if (!isAuthSuccess) {
            return;
        }

        try {
            Message message = Message.builder()
//                    .setNotification(
//                            Notification.builder()
//                                    .setTitle(title)
//                                    .setBody(msg)
//                                    .build()
//                    )
                    .putData("title", title)
                    .putData("body", msg)
                    .setTopic(registrationTopic)
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            log.info("Successfully sent topic message: {}", response);

        } catch (Exception ex) {
            log.error("Failed to send admin notification", ex);
        }
    }
}
