package com.example.heroku.services;

import com.example.heroku.firebase.MyFireBase;
import com.example.heroku.model.UserFCM;
import com.example.heroku.model.repository.UserFCMRepository;
import com.example.heroku.request.client.Notification;
import com.example.heroku.response.Format;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;

@Component
public class UserFCMS {
    @Autowired
    private UserFCMRepository userFCMRepository;

    @Autowired
    MyFireBase myFireBase;

    public Mono<ResponseEntity<Format>> createFCMToken(@Valid @ModelAttribute com.example.heroku.model.UserFCM info) {
        return
                Mono.just(info)
                        .flatMap(userFCM ->
                                userFCMRepository.saveToken(userFCM.getDevice_id(), userFCM.getFcm_id(), userFCM.getStatus(), userFCM.getCreateat())
                        )
                        .flatMap(userFCM ->
                                Mono.just(ok(Format.builder().response(userFCM.getDevice_id()).build()))
                        );
    }

    public Mono<ResponseEntity<Format>> sendNotification(@Valid @ModelAttribute Notification notification) {
        return userFCMRepository.findByDeviceId(notification.getDevice_id())
                .flatMap(userFCM ->
                        Mono.just(myFireBase.SendNotification(notification.getTitle(), notification.getMsg(), userFCM.getFcm_id()))
                )
                .flatMap(response ->
                        Mono.just(ok(Format.builder().response(response).build()))
                );
    }

    public Mono<UserFCM> findByDeviceID(String device_id) {
        return userFCMRepository.findByDeviceId(device_id);
    }

    public Mono<UserFCM> deleteByDeviceID(String device_id) {
        return userFCMRepository.deleteByDeviceId(device_id);
    }
}
