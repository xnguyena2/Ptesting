package com.example.heroku.controller;

import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.response.BuyerData;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.validation.Valid;

@RestController
@RequestMapping("/buyer")
public class BuyerControler {

    @Autowired
    com.example.heroku.services.Buyer buyer;

    @PostMapping("/admin/getall")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<BuyerData> getAll(@RequestBody @Valid SearchQuery query) {
        return buyer.GetAllBeer(query);
    }

}
