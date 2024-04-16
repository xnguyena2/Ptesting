package com.example.heroku.controller;

import com.example.heroku.request.logs.Logs;
import com.example.heroku.response.Format;
import com.example.heroku.util.Util;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/getobjectjs")
public class GetObjectFormat {
    @PostMapping("/logs")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity> getPackageObj(@RequestBody @Valid Logs logs) {
        System.out.println(logs.getLogs());
        return
                Mono.just(ok(Format.builder().response("Done!").build()));
    }
}
