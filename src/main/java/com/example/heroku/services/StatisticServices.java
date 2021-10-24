package com.example.heroku.services;

import com.example.heroku.model.BeerOrder;
import com.example.heroku.model.repository.BeerOrderRepository;
import com.example.heroku.request.beer.SearchQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class StatisticServices {
    @Autowired
    BeerOrderRepository beerOrderRepository;

    public Flux<BeerOrder> getByProductID(SearchQuery query) {
        String dateTxt = query.GetFilterTxt();
        int date = Integer.parseInt(dateTxt);
        return beerOrderRepository.getAllByProductID(query.getQuery(), query.getPage(), query.getSize(), date);
    }

    public Flux<BeerOrder> getAll(SearchQuery query) {
        String dateTxt = query.GetFilterTxt();
        int date = Integer.parseInt(dateTxt);
        return beerOrderRepository.getALL(query.getPage(), query.getSize(), date);
    }
}
