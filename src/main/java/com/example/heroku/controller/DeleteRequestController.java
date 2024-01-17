package com.example.heroku.controller;

import com.example.heroku.model.DeleteRequest;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.carousel.IDContainer;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/deleterequest")
public class DeleteRequestController {

    @Autowired
    private com.example.heroku.services.DeleteRequest deleteRequest;


    @PostMapping("/create")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<DeleteRequest> createRequest(@RequestBody @Valid IDContainer idContainer) {
        return deleteRequest.createRequest(idContainer.getId());
    }


    @PostMapping("/delete")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<DeleteRequest> deleteRequest(@RequestBody @Valid IDContainer idContainer) {

        return deleteRequest.deleteByID(idContainer.getId());
    }

    @PostMapping("/getall")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<DeleteRequest> getAllRequest(@RequestBody @Valid SearchQuery query) {
        return deleteRequest.getAllRequest(query);
    }

}
