package com.example.heroku.services;

import com.example.heroku.model.repository.ProductImportRepository;
import com.example.heroku.request.beer.SearchQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ProductImport {
    @Autowired
    ProductImportRepository productImportRepository;

    public Flux<com.example.heroku.model.ProductImport> getALL(SearchQuery query) {
        String dateTxt = query.GetFilterTxt();
        int date = Integer.parseInt(dateTxt);
        return productImportRepository.getALLBeforeNoDate(query.getGroup_id(), query.getPage(), query.getSize(), date);
    }

    public Flux<com.example.heroku.model.ProductImport> getByProductID(SearchQuery query) {
        String dateTxt = query.GetFilterTxt();
        int date = Integer.parseInt(dateTxt);
        return productImportRepository.getAllByProductID(query.getGroup_id(), query.getQuery(), query.getPage(), query.getSize(), date);
    }
}
