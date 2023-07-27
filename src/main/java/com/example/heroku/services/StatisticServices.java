package com.example.heroku.services;

import com.example.heroku.model.repository.BeerOrderRepository;
import com.example.heroku.model.repository.StatisticsRepository;
import com.example.heroku.model.statistics.StatisticsTotalOrder;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.response.ProductOrderStatus;
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

    public Flux<ProductOrderStatus> getByProductID(SearchQuery query) {
        String dateTxt = query.GetFilterTxt();
        int date = Integer.parseInt(dateTxt);
        return beerOrderRepository.getAllByProductID(query.getGroup_id(), query.getQuery(), query.getPage(), query.getSize(), date);
    }

    public Flux<ProductOrderStatus> getAll(SearchQuery query) {
        String dateTxt = query.GetFilterTxt();
        int date = Integer.parseInt(dateTxt);
        return beerOrderRepository.getALL(query.getGroup_id(), query.getPage(), query.getSize(), date);
    }

    public Mono<StatisticsTotalOrder> getTotal(SearchQuery query){
        String dateTxt = query.GetFilterTxt();
        int date = Integer.parseInt(dateTxt);
        final com.example.heroku.model.PackageOrder.Status status = com.example.heroku.model.PackageOrder.Status.get(query.getQuery());
        return statisticsRepository.getTotal(query.getGroup_id(), date, status);

    }
}
