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
import reactor.core.publisher.Flux;
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
                                userFCMRepository.saveToken(userFCM.getGroup_id(), userFCM.getDevice_id(), userFCM.getFcm_id(), userFCM.getStatus(), userFCM.getCreateat())
                        )
                        .flatMap(userFCM ->
                                Mono.just(ok(Format.builder().response(userFCM.getDevice_id()).build()))
                        );
    }

    public Mono<ResponseEntity<Format>> sendNotification(@Valid @ModelAttribute Notification notification) {
        return sendNotificationToDevice(notification, findByDeviceID(notification.getGroup_id(), notification.getDevice_id()));
    }

    public Mono<ResponseEntity<Format>> sendNotificationToGroup(@Valid @ModelAttribute Notification notification) {
        return sendNotificationToDevice(notification, findByGroupID(notification.getGroup_id()));
    }

    public Mono<ResponseEntity<Format>> sendNotificationToDevice(@Valid @ModelAttribute Notification notification, Flux<UserFCM> userFCMFlux) {
        return userFCMFlux
                .flatMap(userFCM ->
                        Mono.just(myFireBase.SendNotification(notification.getTitle(), notification.getMsg(), userFCM.getFcm_id()))
                )
                .collectList()
                .map(response ->
                        String.join(",", response)
                )
                .flatMap(response ->
                        Mono.just(ok(Format.builder().response(response).build()))
                );
    }

    public Flux<UserFCM> findByDeviceID(String groupid, String device_id) {
        return userFCMRepository.findByDeviceId(groupid, device_id);
    }

    public Flux<UserFCM> findByGroupID(String groupid) {
        return userFCMRepository.findByGroupId(groupid);
    }

    public Mono<UserFCM> deleteByDeviceID(String groupid, String device_id) {
        return userFCMRepository.deleteByDeviceId(groupid, device_id);
    }
}
