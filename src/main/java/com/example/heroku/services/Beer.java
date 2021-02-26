package com.example.heroku.services;

import com.example.heroku.model.BeerUnit;
import com.example.heroku.model.repository.*;
import com.example.heroku.request.beer.BeerInfo;
import com.example.heroku.request.beer.BeerSubmitData;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.beer.SearchResult;
import com.example.heroku.response.Format;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.ArrayList;

import static org.springframework.http.ResponseEntity.ok;

@Component
public class Beer {
    @Autowired
    SearchTokenRepository searchBeer;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    ResultWithCountRepository resultWithCountRepository;

    @Autowired
    BeerUnitRepository beerUnitRepository;

    public Mono<ResponseEntity> generateID() {
        return Mono.just(ok(Format.builder().response(Util.getInstance().GenerateID()).build()));
    }

    public Flux<BeerUnit> CreateBeer(@Valid @ModelAttribute BeerInfo info) {
        return Mono.just(info)
                .flatMap(beerInfo ->
                        this.beerRepository.deleteBySecondId(beerInfo.getBeer().getBeer_second_id())
                )
                .then(Mono.just(info)
                        .flatMap(beerInfo ->
                                this.beerRepository.save(beerInfo.getBeer().UpdateMetaSearch().AutoFill())
                        )
                )
                .flatMap(beer ->
                        this.searchBeer.deleteBySecondId(beer.getBeer_second_id())
                )
                .then(Mono.just(info)
                        .flatMap(beerInfo ->
                                this.searchBeer.saveToken(beerInfo.getBeer().getBeer_second_id(),
                                        beerInfo.getBeer().getTokens()
                                )
                        )
                )
                .then(Mono.just(info)
                        .flatMap(
                                beerInfo ->
                                        this.beerUnitRepository.deleteByBeerId(info.getBeer().getBeer_second_id())
                        )
                )
                .thenMany(Flux.just(info.getBeerUnit())
                        .flatMap(beerUnit ->
                                this.beerUnitRepository.save(beerUnit.AutoFill())
                        )
                );
    }

    public Mono<BeerInfo> GetBeerByID(String id) {
        return beerRepository.findBySecondID(id)
                .map(beer ->
                        BeerInfo.builder().beer(beer).build()
                )
                .flatMap(beerInfo ->
                        Mono.just(new ArrayList<BeerUnit>())
                                .flatMap(beerUnits ->
                                        beerUnitRepository.findByBeerID(id)
                                                .map(beerUnits::add)
                                                .then(Mono.just(beerUnits))
                                                .map(beerInfo::SetBeerUnit)
                                )
                );
    }

    public Flux<BeerSubmitData> GetAllBeer(SearchQuery query) {
        return this.beerRepository.findByIdNotNull(PageRequest.of(query.getPage(), query.getSize(), Sort.by(Sort.Direction.DESC, "createat")))
                .flatMap(this::CoverToSubmitData);
    }

    public Mono<SearchResult> CountGetAllBeer(SearchQuery query) {
        final SearchQuery.Filter filter = query.GetFilter();
        final int page = query.getPage();
        final int size = query.getSize();
        return this.resultWithCountRepository.countAll()
                .map(resultWithCount -> {
                    SearchResult result = new SearchResult();
                    result.setCount(resultWithCount.getCount());
                    return result;
                })
                .flatMap(searchResult ->
                        Flux.just(searchResult).flatMap(rs -> {
                            switch (filter) {
                                case CREATE_ASC:
                                    return this.beerRepository.findByIdNotNull(PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createat")));
                                case CREATE_DESC:
                                    return this.beerRepository.findByIdNotNull(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createat")));
                                case NAME_ASC:
                                    return this.beerRepository.findByIdNotNull(PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name")));
                                case NAME_DESC:
                                    return this.beerRepository.findByIdNotNull(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "name")));
                                case PRICE_ASC:
                                    return this.beerRepository.getAllBeerSortByPriceASC(page, size);
                                case PRICE_DESC:
                                    return this.beerRepository.getAllBeerSortByPriceDESC(page, size);
                                case SOLD_NUM:
                                    return this.beerRepository.getAllBeerSortBySoldDESC(page, size);
                                default:
                                    return this.beerRepository.findByIdNotNull(PageRequest.of(page, size));
                            }
                        })
                                .flatMap(this::CoverToSubmitData)
                                .map(searchResult::Add)
                                .then(Mono.just(searchResult))
                );
    }

    public Mono<BeerSubmitData> CoverToSubmitData(com.example.heroku.model.Beer beer) {
        return Mono.just(BeerSubmitData.builder().build().FromBeer(beer))
                .flatMap(beerSubmitData ->
                        Mono.just(new ArrayList<BeerUnit>())
                                .flatMap(beerUnits ->
                                        beerUnitRepository.findByBeerID(beerSubmitData.getBeerSecondID())
                                                .map(beerUnits::add)
                                                .then(
                                                        Mono.just(beerUnits)
                                                )
                                                .map(beerSubmitData::SetBeerUnit)
                                )
                )
                .flatMap(beerSubmitData ->
                        imageRepository.findByCategory(beerSubmitData.getBeerSecondID())
                                .map(image -> beerSubmitData.AddImage(image.getImgid()))
                                .then(Mono.just(beerSubmitData))
                );
    }

    public Mono<SearchResult> CountSearchBeer(SearchQuery query) {
        final String txt = query.getQuery();
        final int page = query.getPage();
        final int size = query.getSize();
        final SearchQuery.Filter filter = query.GetFilter();
        return Mono.just(txt)
                .map(Util.getInstance()::RemoveAccent)
                .flatMap(searchTxt -> {
                    if (searchTxt.contains("&"))
                        return this.resultWithCountRepository.countSearchBeer(searchTxt)
                                .map(resultWithCount -> {
                                    SearchResult result = SearchResult.builder()
                                            .isNormalSearch(true)
                                            .searchTxt(searchTxt)
                                            .build();
                                    result.setCount(resultWithCount.getCount());
                                    return result;
                                });
                    String finalSearchTxt = "%" + searchTxt + "%";
                    return this.resultWithCountRepository.countSearchBeerLike(finalSearchTxt)
                            .map(resultWithCount -> {
                                SearchResult result = SearchResult.builder()
                                        .isNormalSearch(false)
                                        .searchTxt(finalSearchTxt)
                                        .build();
                                result.setCount(resultWithCount.getCount());
                                return result;
                            });
                })
                .flatMap(searchResult ->
                        Flux.just(searchResult.getSearchTxt()).flatMap(
                                searchTxt -> {
                                    if (searchResult.isNormalSearch()) {
                                        switch (filter){
                                            case CREATE_ASC:
                                                return this.beerRepository.searchBeerSortByCreateASC(searchResult.getSearchTxt(), page, size).flatMap(this::CoverToSubmitData);
                                            case CREATE_DESC:
                                                return this.beerRepository.searchBeerSortByCreateDESC(searchResult.getSearchTxt(), page, size).flatMap(this::CoverToSubmitData);
                                            case NAME_ASC:
                                                return this.beerRepository.searchBeerSortByNameASC(searchResult.getSearchTxt(), page, size).flatMap(this::CoverToSubmitData);
                                            case NAME_DESC:
                                                return this.beerRepository.searchBeerSortByNameDESC(searchResult.getSearchTxt(), page, size).flatMap(this::CoverToSubmitData);
                                            case PRICE_ASC:
                                                return this.beerRepository.searchBeerSortByPriceASC(searchResult.getSearchTxt(), page, size).flatMap(this::CoverToSubmitData);
                                            case PRICE_DESC:
                                                return this.beerRepository.searchBeerSortByPriceDESC(searchResult.getSearchTxt(), page, size).flatMap(this::CoverToSubmitData);
                                            case SOLD_NUM:
                                                return this.beerRepository.searchBeerSortBySoldDESC(searchResult.getSearchTxt(), page, size).flatMap(this::CoverToSubmitData);
                                            default:
                                                return this.beerRepository.searchBeer(searchResult.getSearchTxt(), page, size).flatMap(this::CoverToSubmitData);
                                        }
                                    }
                                    switch (filter){
                                        case CREATE_ASC:
                                            return this.beerRepository.searchBeerLikeSortCreateASC(searchResult.getSearchTxt(), page, size).flatMap(this::CoverToSubmitData);
                                        case CREATE_DESC:
                                            return this.beerRepository.searchBeerLikeSortCreateDESC(searchResult.getSearchTxt(), page, size).flatMap(this::CoverToSubmitData);
                                        case NAME_ASC:
                                            return this.beerRepository.searchBeerLikeSortByNameASC(searchResult.getSearchTxt(), page, size).flatMap(this::CoverToSubmitData);
                                        case NAME_DESC:
                                            return this.beerRepository.searchBeerLikeSortByNameDESC(searchResult.getSearchTxt(), page, size).flatMap(this::CoverToSubmitData);
                                        case PRICE_ASC:
                                            return this.beerRepository.searchBeerLikeSortByPriceASC(searchResult.getSearchTxt(), page, size).flatMap(this::CoverToSubmitData);
                                        case PRICE_DESC:
                                            return this.beerRepository.searchBeerLikeSortByPriceDESC(searchResult.getSearchTxt(), page, size).flatMap(this::CoverToSubmitData);
                                        case SOLD_NUM:
                                            return this.beerRepository.searchBeerLikeSortBySoldDESC(searchResult.getSearchTxt(), page, size).flatMap(this::CoverToSubmitData);
                                        default:
                                            return this.beerRepository.searchBeerLike(searchResult.getSearchTxt(), page, size).flatMap(this::CoverToSubmitData);
                                    }
                                })
                                .map(searchResult::Add)
                                .then(Mono.just(searchResult))
                );
    }

    public Mono<SearchResult> CountGetAllBeerByCategory(SearchQuery query) {
        final com.example.heroku.model.Beer.Category category = com.example.heroku.model.Beer.Category.get(query.getQuery());
        final SearchQuery.Filter filter = query.GetFilter();
        final int page = query.getPage();
        final int size = query.getSize();
        return this.resultWithCountRepository.countCategory(category)
                .map(resultWithCount -> {
                    SearchResult result = new SearchResult();
                    result.setCount(resultWithCount.getCount());
                    return result;
                })
                .flatMap(searchResult ->
                        Flux.just(searchResult).flatMap(rs -> {
                            switch (filter) {
                                case CREATE_ASC:
                                    return this.beerRepository.findByCategory(category, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createat")));
                                case CREATE_DESC:
                                    return this.beerRepository.findByCategory(category, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createat")));
                                case NAME_ASC:
                                    return this.beerRepository.findByCategory(category, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name")));
                                case NAME_DESC:
                                    return this.beerRepository.findByCategory(category, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "name")));
                                case PRICE_ASC:
                                    return this.beerRepository.findByCategoryBeerSortByPriceASC(category, page, size);
                                case PRICE_DESC:
                                    return this.beerRepository.findByCategoryBeerSortByPriceDESC(category, page, size);
                                case SOLD_NUM:
                                    return this.beerRepository.findByCategoryBeerSortBySoldDESC(category, page, size);
                                default:
                                    return this.beerRepository.findByCategory(category, PageRequest.of(page, size));
                            }
                        })
                                .flatMap(this::CoverToSubmitData)
                                .map(searchResult::Add)
                                .then(Mono.just(searchResult))
                );
    }
}
