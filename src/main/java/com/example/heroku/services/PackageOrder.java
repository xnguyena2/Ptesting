package com.example.heroku.services;

import com.example.heroku.model.repository.PackageOrderRepository;
import com.example.heroku.model.repository.ResultWithCountRepository;
import com.example.heroku.request.Order.OrderSearchResult;
import com.example.heroku.request.beer.SearchQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PackageOrder {
    @Autowired
    ResultWithCountRepository resultWithCountRepository;

    @Autowired
    PackageOrderRepository packageOrderRepository;

    public Mono<OrderSearchResult> CountGetAllBeer(SearchQuery query) {
        final SearchQuery.Filter filter = query.GetFilter();
        final int page = query.getPage();
        final int size = query.getSize();
        return this.resultWithCountRepository.countAll()
                .map(resultWithCount -> {
                    OrderSearchResult result = new OrderSearchResult();
                    result.setCount(resultWithCount.getCount());
                    return result;
                })
                .flatMap(orderSearchResult ->
                        packageOrderRepository.getAll(page, size, com.example.heroku.model.PackageOrder.Status.get(query.getQuery()))
                                .map(orderSearchResult::Add)
                                .then(Mono.just(orderSearchResult))
                );
    }
}
