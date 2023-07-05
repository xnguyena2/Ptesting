package com.example.heroku.firebase;

import com.flickr4java.flickr.FlickrException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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

    private boolean isAuthSuccess = false;

    @PostConstruct
    public void init() throws FlickrException {
        this.auth();
    }

    private void auth() {
        if (firebaseCredential == null || firebaseCredential.equals("")) {
            isAuthSuccess = false;
            return;
        }
        try {
            InputStream serviceAccount = new ByteArrayInputStream(firebaseCredential.getBytes());

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
            isAuthSuccess = true;
            System.out.println("initializing firebase success...");
            log.debug("initializing firebase success...");
        } catch (IOException ioEx) {
            System.out.println("initializing firebase error...");
            log.debug("initializing firebase error...");
            ioEx.printStackTrace();
        }

    }

    public boolean isAuthSuccess() {
        return isAuthSuccess;
    }

    public String SendNotification(String title, String msg, String token) {

        if (!isAuthSuccess) {
            return "not AuthSuccess";
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
                    .setToken(token)
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);

            System.out.println("Successfully sent message: " + response);
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "Not success!";
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

            System.out.println("Successfully sent message: " + response);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
