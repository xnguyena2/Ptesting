package com.example.heroku.services;

import com.example.heroku.model.PackageOrder;
import com.example.heroku.model.repository.BuyerRepository;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.response.BuyerData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class Buyer {
    @Autowired
    BuyerRepository buyerRepository;

    public Mono<com.example.heroku.model.Buyer> createBuyer(com.example.heroku.model.Buyer buyerData) {
        return this.buyerRepository.deleteByPhone(buyerData.getGroup_id(), buyerData.getPhone_number())
                .then(this.buyerRepository.save(buyerData));
    }

    public Flux<BuyerData> GetAll(SearchQuery query) {
        PackageOrder.Status status = PackageOrder.Status.get(query.GetFilterTxt());
        return this.buyerRepository.getAll(status, query.getGroup_id(), query.getPage(), query.getSize())
                .map(BuyerData::new);
    }

    public Flux<BuyerData> GetAllDirect(SearchQuery query) {
        return this.buyerRepository.findByGroupID(query.getGroup_id(), query.getPage(), query.getSize())
                .map(BuyerData::new);
    }


    public Flux<BuyerData> FindByPhone(SearchQuery query) {
        String phone = "%" + query.getQuery() + "%";
        return this.buyerRepository.findByPhoneOrDeviceIDContains(query.getGroup_id(), phone, query.getPage(), query.getSize())
                .map(BuyerData::new);
    }

    public Mono<com.example.heroku.model.Buyer> deleteBuyer(String groupID, String phone) {
        return this.buyerRepository.deleteByPhone(groupID, phone);
    }
}
