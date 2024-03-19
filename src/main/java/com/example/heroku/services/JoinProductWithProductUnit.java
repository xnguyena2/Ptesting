package com.example.heroku.services;

import com.example.heroku.model.joinwith.ProductJoinWithProductUnit;
import com.example.heroku.model.repository.JoinProductWithProductUnitRepository;
import com.example.heroku.request.beer.BeerSubmitData;
import com.example.heroku.request.beer.SearchQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class JoinProductWithProductUnit {

    @Autowired
    JoinProductWithProductUnitRepository joinProductWithProductUnitRepository;


    private Flux<BeerSubmitData> flattenResult(Flux<ProductJoinWithProductUnit> joinResult) {
        return joinResult.groupBy(ProductJoinWithProductUnit::getProduct_second_id)
                .flatMap(groupedFlux -> groupedFlux.collectList().map(ProductJoinWithProductUnit::GenerateBeerSubmitData));
    }

    public Flux<BeerSubmitData> GetAllBeerByJoinFirstNotHide(SearchQuery query) {
        return flattenResult(this.joinProductWithProductUnitRepository.getIfProductNotHide(query.getGroup_id(), query.getPage(), query.getSize()));
    }

    public Flux<BeerSubmitData> GetAllBeerByJoinFirstNotHideForWeb(SearchQuery query) {
        return flattenResult(this.joinProductWithProductUnitRepository.getIfProductNotHideAndForWeb(query.getGroup_id(), query.getPage(), query.getSize()));
    }
}
