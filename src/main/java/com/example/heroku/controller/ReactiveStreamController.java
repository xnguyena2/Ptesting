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

    @GetMapping("/test")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity> generateID() {
        severEventAdapter.SendEvent("hel0000000");
        return Mono.just(ok(Format.builder().response(Util.getInstance().GenerateID()).build()));
    }

    @GetMapping(path = "/stream-flux")
    public Flux<ServerSentEvent<String>> streamFlux() {

        return severEventAdapter.FolkEvent()
                .map(sequence -> ServerSentEvent.<String> builder()
                        .id(String.valueOf(sequence))
                        .event("periodic-event")
                        .data("SSE - " + LocalTime.now().toString())
                        .build());
    }
}
