package com.example.heroku.services;

import com.example.heroku.model.joinwith.ProductJoinWithProductUnit;
import com.example.heroku.model.repository.JoinProductWithProductUnitRepository;
import com.example.heroku.request.beer.BeerSubmitData;
import com.example.heroku.request.beer.SearchQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@Component
public class JoinProductWithProductUnit {

    @Autowired
    JoinProductWithProductUnitRepository joinProductWithProductUnitRepository;


    private Flux<BeerSubmitData> flattenResult(Flux<ProductJoinWithProductUnit> joinResult) {
        return joinResult.groupBy(ProductJoinWithProductUnit::getProduct_second_id)
                .flatMap(groupedFlux -> groupedFlux.collectList().map(ProductJoinWithProductUnit::GenerateBeerSubmitData));
    }

    private Flux<BeerSubmitData> flattenResultHard(Flux<ProductJoinWithProductUnit> joinResult) {
        return joinResult.collectList().map(productJoinWithProductUnits -> {
                    Map<String, java.util.List<ProductJoinWithProductUnit>> productJoinWithProductUnitHashMap = new HashMap<>();
                    productJoinWithProductUnits.forEach(productJoinWithProductUnit -> {
                        String key = productJoinWithProductUnit.getProduct_second_id();
                        if (productJoinWithProductUnitHashMap.containsKey(key)) {
                            productJoinWithProductUnitHashMap.get(key).add(productJoinWithProductUnit);
                        } else {
                            java.util.List<ProductJoinWithProductUnit> newList =  new ArrayList<>();
                            newList.add(productJoinWithProductUnit);
                            productJoinWithProductUnitHashMap.put(key, newList);
                        }
                    });
                    return (java.util.List<java.util.List<ProductJoinWithProductUnit>>) new ArrayList<>(productJoinWithProductUnitHashMap.values());
                })
                .flatMapMany(Flux::fromIterable)
//                .flatMapIterable(list -> list)
                .map(ProductJoinWithProductUnit::GenerateBeerSubmitData);
    }

    private Mono<BeerSubmitData> flattenResult(Mono<ProductJoinWithProductUnit> joinResult) {
        return joinResult.map(productJoinWithProductUnit -> ProductJoinWithProductUnit.GenerateBeerSubmitData(Collections.singletonList(productJoinWithProductUnit)));
    }

    private Mono<BeerSubmitData> flattenResultFor1Product(Flux<ProductJoinWithProductUnit> joinResult) {
        return joinResult.collectList()
                .map(ProductJoinWithProductUnit::GenerateBeerSubmitData);
    }

    public Flux<BeerSubmitData> GetAllBeerByJoinFirstNotHide(SearchQuery query) {
        return flattenResultHard(this.joinProductWithProductUnitRepository.getIfProductNotHide(query.getGroup_id(), query.getPage(), query.getSize()));
    }

    public Flux<BeerSubmitData> GetAllBeerByJoinFirstNotHideForWeb(SearchQuery query) {
        return flattenResult(this.joinProductWithProductUnitRepository.getIfProductNotHideAndForWeb(query.getGroup_id(), query.getPage(), query.getSize()));
    }

    public Mono<BeerSubmitData> GetProductAndUnit(String groupID, String productID, String productUnitID) {
        return flattenResult(this.joinProductWithProductUnitRepository.getProductAndOnly1Unit(groupID, productID, productUnitID));
    }

    public Flux<BeerSubmitData> GetAllBeerByJoinOfPackage(String groupID, String packageID) {
        return this.joinProductWithProductUnitRepository.getProductAndUnitOfPackage(groupID, packageID)
                .map(productJoinWithProductUnit -> ProductJoinWithProductUnit.GenerateBeerSubmitData(Collections.singletonList(productJoinWithProductUnit)));
    }

    public Mono<BeerSubmitData> GetProductAndAllUnit(String groupID, String productID) {
        return flattenResultFor1Product(this.joinProductWithProductUnitRepository.getProductAndAllUnit(groupID, productID));
    }
}
