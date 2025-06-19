package com.example.heroku.services;

import com.example.heroku.model.repository.ProductSerialRepository;
import com.example.heroku.request.carousel.IDContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class ProductSerial {

    @Autowired
    ProductSerialRepository productSerialRepository;

    public Flux<com.example.heroku.model.ProductSerial> getAllSerial(IDContainer idContainer) {
        return this.productSerialRepository.getByGroupID(idContainer.getGroup_id());
    }

}
