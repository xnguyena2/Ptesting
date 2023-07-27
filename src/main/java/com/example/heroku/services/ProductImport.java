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
        return productImportRepository.getALL(query.getGroup_id(), query.getPage(), query.getSize(), date);
    }

    public Flux<com.example.heroku.model.ProductImport> getByProductID(SearchQuery query) {
        String dateTxt = query.GetFilterTxt();
        int date = Integer.parseInt(dateTxt);
        return productImportRepository.getAllByProductID(query.getGroup_id(), query.getQuery(), query.getPage(), query.getSize(), date);
    }

    public Mono<com.example.heroku.model.ProductImport> addNewRecord(com.example.heroku.model.ProductImport newRecord) {
        return productImportRepository.deleteByImportID(newRecord.getGroup_id(), newRecord.getProduct_import_second_id())
                .then(productImportRepository.save(newRecord.AutoFill()));
    }

    public Mono<com.example.heroku.model.ProductImport> deleteRecord(String groupid, String id) {
        return productImportRepository.deleteByImportID(groupid, id);
    }
}
