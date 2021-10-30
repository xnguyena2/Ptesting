package com.example.heroku.services;

import com.example.heroku.model.PackageOrder;
import com.example.heroku.model.repository.BuyerRepository;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.response.BuyerData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class Buyer {
    @Autowired
    BuyerRepository buyerRepository;


    public Flux<BuyerData> GetAll(SearchQuery query) {
        PackageOrder.Status status = PackageOrder.Status.get(query.GetFilterTxt());
        return this.buyerRepository.getAll(status, query.getPage(), query.getSize())
                .map(BuyerData::new);
    }
}
