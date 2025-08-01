package com.example.heroku.services;

import com.example.heroku.model.repository.ProductImportPriceRepository;
import com.example.heroku.request.warehouse.SearchImportQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class ProductPriceChange {

    @Autowired
    ProductImportPriceRepository productImportPriceRepository;

    public Flux<com.example.heroku.model.productprice.ProductPriceChange> GetPriceRange(SearchImportQuery query) {
        return productImportPriceRepository.getPriceRangeOfType(query.getGroup_id(), query.getFrom(), query.getTo(), query.getType(), query.getStatus());
    }
}
