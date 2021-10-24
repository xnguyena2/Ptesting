package com.example.heroku.controller;

import com.example.heroku.response.Format;
import com.example.heroku.util.Util;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/util")
public class Utils {
    @GetMapping("/admin/generateid")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Format>> generateID() {
        return Mono.just(ok(Format.builder().response(Util.getInstance().GenerateID()).build()));
    }
}
