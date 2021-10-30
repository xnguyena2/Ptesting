package com.example.heroku.services;

import com.example.heroku.model.BeerOrder;
import com.example.heroku.model.PackageOrder;
import com.example.heroku.model.repository.BeerOrderRepository;
import com.example.heroku.model.repository.StatisticsRepository;
import com.example.heroku.model.statistics.StatisticsTotalOrder;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.response.BeerOrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class StatisticServices {
    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    StatisticsRepository statisticsRepository;

    public Flux<BeerOrderStatus> getByProductID(SearchQuery query) {
        String dateTxt = query.GetFilterTxt();
        int date = Integer.parseInt(dateTxt);
        return beerOrderRepository.getAllByProductID(query.getQuery(), query.getPage(), query.getSize(), date);
    }

    public Flux<BeerOrderStatus> getAll(SearchQuery query) {
        String dateTxt = query.GetFilterTxt();
        int date = Integer.parseInt(dateTxt);
        return beerOrderRepository.getALL(query.getPage(), query.getSize(), date);
    }

    public Mono<StatisticsTotalOrder> getTotal(SearchQuery query){
        String dateTxt = query.GetFilterTxt();
        int date = Integer.parseInt(dateTxt);
        final com.example.heroku.model.PackageOrder.Status status = com.example.heroku.model.PackageOrder.Status.get(query.getQuery());
        return statisticsRepository.getTotal(date, status);

    }
}
