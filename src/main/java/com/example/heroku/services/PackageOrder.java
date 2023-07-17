package com.example.heroku.services;

import com.example.heroku.model.ProductOrder;
import com.example.heroku.model.repository.BeerOrderRepository;
import com.example.heroku.model.repository.BeerUnitOrderRepository;
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

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    BeerUnitOrderRepository beerUnitOrderRepository;

    public Mono<OrderSearchResult> CountGetAllOrder(SearchQuery query) {
        final String dateTxt = query.GetFilterTxt();
        final int page = query.getPage();
        final int size = query.getSize();
        final com.example.heroku.model.PackageOrder.Status status = com.example.heroku.model.PackageOrder.Status.get(query.getQuery());
        final int date = Integer.parseInt(dateTxt);
        return this.resultWithCountRepository.countPackageOrder(status, date)
                .map(resultWithCount -> {
                    OrderSearchResult result = new OrderSearchResult();
                    result.setCount(resultWithCount.getCount());
                    return result;
                })
                .flatMap(orderSearchResult ->
                        packageOrderRepository.getAll(page, size, status, date)
                                .flatMap(this::coverToData)
                                .map(orderSearchResult::Add)
                                .then(Mono.just(orderSearchResult))
                );
    }

    Mono<OrderSearchResult.PackageOrderData> coverToData(com.example.heroku.model.PackageOrder packageOrder) {
        return Mono.just(packageOrder)
                .map(OrderSearchResult.PackageOrderData::new)
                .flatMap(packageOrderData ->
                        beerOrderRepository.findBySecondID(packageOrderData.getPackage_order_second_id())
                                .distinct(ProductOrder::getProduct_second_id)
                                .map(OrderSearchResult.PackageOrderData.BeerOrderData::new)
                                .map(packageOrderData::Add)
                                .flatMap(beerOrderData ->
                                        beerUnitOrderRepository.findByBeerAndOrder(packageOrderData.getPackage_order_second_id(), beerOrderData.getProduct_second_id())
                                                .map(beerOrderData::Add)
                                )
                                .then(Mono.just(packageOrderData))
                );
    }

    public Mono<OrderSearchResult.PackageOrderData> getOrderDetail(String id){
        return packageOrderRepository.getByID(id)
                .flatMap(this::coverToData);
    }

    public Mono<com.example.heroku.model.PackageOrder> UpdateStatus(String id, com.example.heroku.model.PackageOrder.Status status) {
        return packageOrderRepository.changeStatus(id, status);
    }
}
