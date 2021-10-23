package com.example.heroku.services;

import com.example.heroku.model.repository.ProductImportRepository;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.response.Format;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.ResponseEntity.ok;

@Component
public class ProductImport {
    @Autowired
    ProductImportRepository productImportRepository;

    public Mono<ResponseEntity<Format>> generateID() {
        return Mono.just(ok(Format.builder().response(Util.getInstance().GenerateID()).build()));
    }

    public Flux<com.example.heroku.model.ProductImport> getALL(SearchQuery query) {
        String dateTxt = query.GetFilterTxt();
        int date = Integer.parseInt(dateTxt);
        return productImportRepository.getALL(query.getPage(), query.getSize(), date);
    }

    public Flux<com.example.heroku.model.ProductImport> getByProductID(SearchQuery query) {
        String dateTxt = query.GetFilterTxt();
        int date = Integer.parseInt(dateTxt);
        return productImportRepository.getAllByProductID(query.getQuery(), query.getPage(), query.getSize(), date);
    }

    public Mono<com.example.heroku.model.ProductImport> addNewRecord(com.example.heroku.model.ProductImport newRecord) {
        return productImportRepository.deleteByImportID(newRecord.getProduct_import_second_id())
                .then(productImportRepository.save(newRecord.AutoFill()));
    }

    public Mono<com.example.heroku.model.ProductImport> deleteRecord(String id) {
        return productImportRepository.deleteByImportID(id);
    }
}
