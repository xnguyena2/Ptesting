package com.example.heroku.controller;

import com.example.heroku.response.Format;
import com.example.heroku.services.SeverEventAdapterImpl;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalTime;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/stream")
public class ReactiveStreamController {


    @Autowired
    SeverEventAdapterImpl severEventAdapter;

//    @GetMapping("/test")
//    @CrossOrigin(origins = Util.HOST_URL)
//    public Mono<ResponseEntity<Format>> generateID() {
//        severEventAdapter.SendEvent("hel0000000");
//        return Mono.just(ok(Format.builder().response("test").build()));
//    }

    @GetMapping(path = "admin/order")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<ServerSentEvent> streamFlux() {

        Disposable heartBeat = Flux.interval(Duration.ofSeconds(10)).map(timg -> {
            severEventAdapter.SendEvent("heart beat");
            System.out.println("Server keep alive heart beat!");
            return timg;
        }).subscribe();

        return severEventAdapter.FolkEvent(heartBeat::dispose)
                .map(sequence -> {
                            if (sequence.equals("heart beat")) {
                                return ServerSentEvent.builder()
                                        .id(Util.getInstance().GenerateID())
                                        .comment("keep alive")
                                        .build();
                            }
                            return ServerSentEvent.builder()
                                    .id(Util.getInstance().GenerateID())
                                    .event("message")
                                    .data(sequence)
                                    .build();
                        }
                );
    }
}
