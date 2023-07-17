package com.example.heroku.services;

import com.example.heroku.model.ProductUnit;
import com.example.heroku.model.Product;
import com.example.heroku.model.repository.*;
import com.example.heroku.request.beer.BeerInfo;
import com.example.heroku.request.beer.BeerSubmitData;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.beer.SearchResult;
import com.example.heroku.request.carousel.IDContainer;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.ArrayList;

@Component
public class Beer {
    @Autowired
    com.example.heroku.services.Image imageAPI;

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

    public Flux<ProductUnit> CreateBeer(@Valid @ModelAttribute BeerInfo info) {
        return Mono.just(info)
                .flatMap(beerInfo ->
                        this.beerRepository.deleteBySecondId(beerInfo.getProduct().getProduct_second_id())
                )
                .then(Mono.just(info)
                        .flatMap(beerInfo ->
                                this.beerRepository.save(beerInfo.getProduct().UpdateMetaSearch().AutoFill())
                        )
                )
                .flatMap(beer ->
                        this.searchBeer.deleteBySecondId(beer.getProduct_second_id())
                )
                .then(Mono.just(info)
                        .flatMap(beerInfo ->
                                this.searchBeer.saveToken(beerInfo.getProduct().getProduct_second_id(),
                                        beerInfo.getProduct().getTokens()
                                )
                        )
                )
                .then(Mono.just(info)
                        .flatMap(
                                beerInfo ->
                                        this.beerUnitRepository.deleteByBeerId(info.getProduct().getProduct_second_id())
                        )
                )
                .thenMany(Flux.just(info.getProductUnit())
                        .flatMap(beerUnit ->
                                this.beerUnitRepository.save(beerUnit.AutoFill())
                        )
                );
    }

    public Mono<Product> DeleteBeerByID(String id) {
        return imageRepository.findByCategory(id)
                .flatMap(image -> imageAPI.Delete(IDContainer.builder().id(image.getImgid()).build()))
                .then(
                        Mono.just(id)
                        .flatMap(
                                beerInfo ->
                                        this.beerUnitRepository.deleteByBeerId(id)
                        )
                )
                .then(
                        Mono.just(id)
                        .flatMap(
                                beerInfo ->
                                        beerRepository.deleteBySecondId(id)
                        )
                );
    }

    public Mono<BeerSubmitData> GetBeerByID(String id) {
        return beerRepository.findBySecondID(id)
                .flatMap(this::CoverToSubmitData);
    }

    public Flux<BeerSubmitData> GetAllBeer(SearchQuery query) {
        return this.beerRepository.findByIdNotNull(query.getPage(), query.getSize())
                .flatMap(this::CoverToSubmitData);
    }

    public Mono<SearchResult<BeerSubmitData>> AdminCountGetAllBeer(SearchQuery query) {
        final SearchQuery.Filter filter = query.GetFilter();
        final int page = query.getPage();
        final int size = query.getSize();
        return this.resultWithCountRepository.adminCountAll()
                .map(resultWithCount -> {
                    SearchResult<BeerSubmitData> result = new SearchResult<>();
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
                                            return this.beerRepository.adminGetAllBeerSortByPriceASC(page, size);
                                        case PRICE_DESC:
                                            return this.beerRepository.adminGetAllBeerSortByPriceDESC(page, size);
                                        case SOLD_NUM:
                                            return this.beerRepository.adminGetAllBeerSortBySoldDESC(page, size);
                                        default:
                                            return this.beerRepository.findByIdNotNull(PageRequest.of(page, size));
                                    }
                                })
                                .flatMap(this::CoverToSubmitData)
                                .map(searchResult::Add)
                                .then(Mono.just(searchResult))
                );
    }

    public Mono<SearchResult<BeerSubmitData>> CountGetAllBeer(SearchQuery query) {
        final SearchQuery.Filter filter = query.GetFilter();
        final int page = query.getPage();
        final int size = query.getSize();
        return this.resultWithCountRepository.countAll()
                .map(resultWithCount -> {
                    SearchResult<BeerSubmitData> result = new SearchResult<>();
                    result.setCount(resultWithCount.getCount());
                    return result;
                })
                .flatMap(searchResult ->
                        Flux.just(searchResult).flatMap(rs -> {
                            switch (filter) {
                                case CREATE_ASC:
                                    return this.beerRepository.findByIdNotNullASC(page, size, "createat");
                                case CREATE_DESC:
                                    return this.beerRepository.findByIdNotNullDESC(page, size,  "createat");
                                case NAME_ASC:
                                    return this.beerRepository.findByIdNotNullASC(page, size, "name");
                                case NAME_DESC:
                                    return this.beerRepository.findByIdNotNullDESC(page, size,  "name");
                                case PRICE_ASC:
                                    return this.beerRepository.getAllBeerSortByPriceASC(page, size);
                                case PRICE_DESC:
                                    return this.beerRepository.getAllBeerSortByPriceDESC(page, size);
                                case SOLD_NUM:
                                    return this.beerRepository.getAllBeerSortBySoldDESC(page, size);
                                default:
                                    return this.beerRepository.findByIdNotNull(page, size);
                            }
                        })
                                .flatMap(this::CoverToSubmitData)
                                .map(searchResult::Add)
                                .then(Mono.just(searchResult))
                );
    }

    public Mono<BeerSubmitData> CoverToSubmitData(Product product) {
        return Mono.just(BeerSubmitData.builder().build().FromBeer(product))
                .flatMap(beerSubmitData ->
                        Mono.just(new ArrayList<ProductUnit>())
                                .flatMap(beerUnits ->
                                        beerUnitRepository.findByBeerID(beerSubmitData.getBeerSecondID())
                                                .map(ProductUnit::CheckDiscount)
                                                .map(beerUnits::add)
                                                .then(
                                                        Mono.just(beerUnits)
                                                )
                                                .map(beerSubmitData::SetBeerUnit)
                                )
                )
                .flatMap(beerSubmitData ->
                        imageRepository.findByCategory(beerSubmitData.getBeerSecondID())
                                .map(beerSubmitData::AddImage)
                                .then(Mono.just(beerSubmitData))
                );
    }

    public Mono<SearchResult<BeerSubmitData>> AdminCountSearchBeer(SearchQuery query) {
        final String txt = query.getQuery();
        final int page = query.getPage();
        final int size = query.getSize();
        final SearchQuery.Filter filter = query.GetFilter();
        return Mono.just(txt)
                .map(Util.getInstance()::RemoveAccent)
                .flatMap(searchTxt -> {
                    if (searchTxt.contains("&"))
                        return this.resultWithCountRepository.adminCountSearchBeer(searchTxt)
                                .map(resultWithCount -> {
                                    SearchResult<BeerSubmitData> result = SearchResult.<BeerSubmitData>builder()
                                            .isNormalSearch(true)
                                            .searchTxt(searchTxt)
                                            .build();
                                    result.setCount(resultWithCount.getCount());
                                    return result;
                                });
                    String finalSearchTxt = "%" + searchTxt + "%";
                    return this.resultWithCountRepository.adminCountSearchBeerLike(finalSearchTxt)
                            .map(resultWithCount -> {
                                SearchResult<BeerSubmitData> result = SearchResult.<BeerSubmitData>builder()
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
                                                        return this.beerRepository.adminSearchBeerSortByCreateASC(searchResult.getSearchTxt(), page, size);
                                                    case CREATE_DESC:
                                                        return this.beerRepository.adminSearchBeerSortByCreateDESC(searchResult.getSearchTxt(), page, size);
                                                    case NAME_ASC:
                                                        return this.beerRepository.adminSearchBeerSortByNameASC(searchResult.getSearchTxt(), page, size);
                                                    case NAME_DESC:
                                                        return this.beerRepository.adminSearchBeerSortByNameDESC(searchResult.getSearchTxt(), page, size);
                                                    case PRICE_ASC:
                                                        return this.beerRepository.adminSearchBeerSortByPriceASC(searchResult.getSearchTxt(), page, size);
                                                    case PRICE_DESC:
                                                        return this.beerRepository.adminSearchBeerSortByPriceDESC(searchResult.getSearchTxt(), page, size);
                                                    case SOLD_NUM:
                                                        return this.beerRepository.adminSearchBeerSortBySoldDESC(searchResult.getSearchTxt(), page, size);
                                                    default:
                                                        return this.beerRepository.adminSearchBeer(searchResult.getSearchTxt(), page, size);
                                                }
                                            }
                                            switch (filter){
                                                case CREATE_ASC:
                                                    return this.beerRepository.adminSearchBeerLikeSortCreateASC(searchResult.getSearchTxt(), page, size);
                                                case CREATE_DESC:
                                                    return this.beerRepository.adminSearchBeerLikeSortCreateDESC(searchResult.getSearchTxt(), page, size);
                                                case NAME_ASC:
                                                    return this.beerRepository.adminSearchBeerLikeSortByNameASC(searchResult.getSearchTxt(), page, size);
                                                case NAME_DESC:
                                                    return this.beerRepository.adminSearchBeerLikeSortByNameDESC(searchResult.getSearchTxt(), page, size);
                                                case PRICE_ASC:
                                                    return this.beerRepository.adminSearchBeerLikeSortByPriceASC(searchResult.getSearchTxt(), page, size);
                                                case PRICE_DESC:
                                                    return this.beerRepository.adminSearchBeerLikeSortByPriceDESC(searchResult.getSearchTxt(), page, size);
                                                case SOLD_NUM:
                                                    return this.beerRepository.adminSearchBeerLikeSortBySoldDESC(searchResult.getSearchTxt(), page, size);
                                                default:
                                                    return this.beerRepository.adminSearchBeerLike(searchResult.getSearchTxt(), page, size);
                                            }
                                        })
                                .flatMap(this::CoverToSubmitData)
                                .map(searchResult::Add)
                                .then(Mono.just(searchResult))
                );
    }

    public Mono<SearchResult<BeerSubmitData>> CountSearchBeer(SearchQuery query) {
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
                                    SearchResult<BeerSubmitData> result = SearchResult.<BeerSubmitData>builder()
                                            .isNormalSearch(true)
                                            .searchTxt(searchTxt)
                                            .build();
                                    result.setCount(resultWithCount.getCount());
                                    return result;
                                });
                    String finalSearchTxt = "%" + searchTxt + "%";
                    return this.resultWithCountRepository.countSearchBeerLike(finalSearchTxt)
                            .map(resultWithCount -> {
                                SearchResult<BeerSubmitData> result = SearchResult.<BeerSubmitData>builder()
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
                                                return this.beerRepository.searchBeerSortByCreateASC(searchResult.getSearchTxt(), page, size);
                                            case CREATE_DESC:
                                                return this.beerRepository.searchBeerSortByCreateDESC(searchResult.getSearchTxt(), page, size);
                                            case NAME_ASC:
                                                return this.beerRepository.searchBeerSortByNameASC(searchResult.getSearchTxt(), page, size);
                                            case NAME_DESC:
                                                return this.beerRepository.searchBeerSortByNameDESC(searchResult.getSearchTxt(), page, size);
                                            case PRICE_ASC:
                                                return this.beerRepository.searchBeerSortByPriceASC(searchResult.getSearchTxt(), page, size);
                                            case PRICE_DESC:
                                                return this.beerRepository.searchBeerSortByPriceDESC(searchResult.getSearchTxt(), page, size);
                                            case SOLD_NUM:
                                                return this.beerRepository.searchBeerSortBySoldDESC(searchResult.getSearchTxt(), page, size);
                                            default:
                                                return this.beerRepository.searchBeer(searchResult.getSearchTxt(), page, size);
                                        }
                                    }
                                    switch (filter){
                                        case CREATE_ASC:
                                            return this.beerRepository.searchBeerLikeSortCreateASC(searchResult.getSearchTxt(), page, size);
                                        case CREATE_DESC:
                                            return this.beerRepository.searchBeerLikeSortCreateDESC(searchResult.getSearchTxt(), page, size);
                                        case NAME_ASC:
                                            return this.beerRepository.searchBeerLikeSortByNameASC(searchResult.getSearchTxt(), page, size);
                                        case NAME_DESC:
                                            return this.beerRepository.searchBeerLikeSortByNameDESC(searchResult.getSearchTxt(), page, size);
                                        case PRICE_ASC:
                                            return this.beerRepository.searchBeerLikeSortByPriceASC(searchResult.getSearchTxt(), page, size);
                                        case PRICE_DESC:
                                            return this.beerRepository.searchBeerLikeSortByPriceDESC(searchResult.getSearchTxt(), page, size);
                                        case SOLD_NUM:
                                            return this.beerRepository.searchBeerLikeSortBySoldDESC(searchResult.getSearchTxt(), page, size);
                                        default:
                                            return this.beerRepository.searchBeerLike(searchResult.getSearchTxt(), page, size);
                                    }
                                })
                                .flatMap(this::CoverToSubmitData)
                                .map(searchResult::Add)
                                .then(Mono.just(searchResult))
                );
    }

    public Mono<SearchResult<BeerSubmitData>> AdminCountGetAllBeerByCategory(SearchQuery query) {
        final Product.Category category = Product.Category.get(query.getQuery());
        final SearchQuery.Filter filter = query.GetFilter();
        final int page = query.getPage();
        final int size = query.getSize();
        return this.resultWithCountRepository.AdminCountCategory(category)
                .map(resultWithCount -> {
                    SearchResult<BeerSubmitData> result = new SearchResult<>();
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
                                            return this.beerRepository.AdminFindByCategoryBeerSortByPriceASC(category, page, size);
                                        case PRICE_DESC:
                                            return this.beerRepository.AdminFindByCategoryBeerSortByPriceDESC(category, page, size);
                                        case SOLD_NUM:
                                            return this.beerRepository.AdminFindByCategoryBeerSortBySoldDESC(category, page, size);
                                        default:
                                            return this.beerRepository.findByCategory(category, PageRequest.of(page, size));
                                    }
                                })
                                .flatMap(this::CoverToSubmitData)
                                .map(searchResult::Add)
                                .then(Mono.just(searchResult))
                );
    }

    public Mono<SearchResult<BeerSubmitData>> CountGetAllBeerByCategory(SearchQuery query) {
        final Product.Category category = Product.Category.get(query.getQuery());
        final SearchQuery.Filter filter = query.GetFilter();
        final int page = query.getPage();
        final int size = query.getSize();
        return this.resultWithCountRepository.countCategory(category)
                .map(resultWithCount -> {
                    SearchResult<BeerSubmitData> result = new SearchResult<>();
                    result.setCount(resultWithCount.getCount());
                    return result;
                })
                .flatMap(searchResult ->
                        Flux.just(searchResult).flatMap(rs -> {
                            switch (filter) {
                                case CREATE_ASC:
                                    return this.beerRepository.findByCategoryASC(category, page, size, "createat");
                                case CREATE_DESC:
                                    return this.beerRepository.findByCategoryDESC(category, page, size, "createat");
                                case NAME_ASC:
                                    return this.beerRepository.findByCategoryASC(category, page, size, "name");
                                case NAME_DESC:
                                    return this.beerRepository.findByCategoryDESC(category, page, size, "name");
                                case PRICE_ASC:
                                    return this.beerRepository.findByCategoryBeerSortByPriceASC(category, page, size);
                                case PRICE_DESC:
                                    return this.beerRepository.findByCategoryBeerSortByPriceDESC(category, page, size);
                                case SOLD_NUM:
                                    return this.beerRepository.findByCategoryBeerSortBySoldDESC(category, page, size);
                                default:
                                    return this.beerRepository.findByCategoryDESC(category, page, size, "createat");
                            }
                        })
                                .flatMap(this::CoverToSubmitData)
                                .map(searchResult::Add)
                                .then(Mono.just(searchResult))
                );
    }
}
