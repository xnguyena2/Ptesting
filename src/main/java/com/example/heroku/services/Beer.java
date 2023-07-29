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
                .map(beerInfo -> beerInfo.CorrectData().getProduct().UpdateMetaSearch().AutoFill())
                .flatMap(product ->
                        this.beerRepository.saveProduct(product.getGroup_id(), product.getProduct_second_id(),
                                product.getName(), product.getDetail(),
                                product.getCategory(), product.getMeta_search(),
                                product.getStatus() == null ? null : product.getStatus().getName(), product.getCreateat()
                        ).then(Mono.just(product))
                )
                .flatMap(product -> this.searchBeer.saveToken(product.getGroup_id(), product.getProduct_second_id(),
                        product.getTokens(), product.getCreateat())
                )
                .then(Mono.just(info.getProduct())
                        .flatMap(product -> this.beerUnitRepository.deleteByBeerId(product.getGroup_id(), product.getProduct_second_id()))
                )
                .thenMany(Flux.just(info.getProductUnit())
                                .map(ProductUnit::AutoFill)
                                .flatMap(beerUnit -> this.beerUnitRepository.save(beerUnit)
//                                this.beerUnitRepository.saveProductUnit(beerUnit.getGroup_id(), beerUnit.getProduct_second_id(), beerUnit.getProduct_unit_second_id(),
//                                                beerUnit.getName(), beerUnit.getPrice(),
//                                                beerUnit.getDiscount(), beerUnit.getDate_expire(),
//                                                beerUnit.getVolumetric(), beerUnit.getWeight(),
//                                                beerUnit.getStatus() == null ? null : beerUnit.getStatus().getName(), beerUnit.getCreateat()
//                                        )
//                                        .then(Mono.just(beerUnit))
                                )
                );
    }

    public Mono<Product> DeleteBeerByID(String groupID, String id) {
        return imageRepository.findByCategory(groupID, id)
                .flatMap(image -> imageAPI.Delete(IDContainer.builder().group_id(groupID).id(image.getImgid()).build()))
                .then(
                        Mono.just(id)
                        .flatMap(
                                beerInfo ->
                                        this.beerUnitRepository.deleteByBeerId(groupID, id)
                        )
                )
                .then(
                        Mono.just(id)
                        .flatMap(
                                beerInfo ->
                                        beerRepository.deleteBySecondId(groupID, id)
                        )
                );
    }

    public Mono<BeerSubmitData> GetBeerByID(String  groupID, String id) {
        return beerRepository.findBySecondID(groupID, id)
                .flatMap(this::CoverToSubmitData);
    }

    public Flux<BeerSubmitData> GetAllBeer(SearchQuery query) {
        return this.beerRepository.findByIdNotNull(query.getGroup_id(), query.getPage(), query.getSize())
                .flatMap(this::CoverToSubmitData);
    }

    public Mono<SearchResult<BeerSubmitData>> AdminCountGetAllBeer(SearchQuery query) {
        final SearchQuery.Filter filter = query.GetFilter();
        final int page = query.getPage();
        final int size = query.getSize();
        return this.resultWithCountRepository.adminCountAll(query.getGroup_id())
                .map(resultWithCount -> {
                    SearchResult<BeerSubmitData> result = new SearchResult<>();
                    result.setCount(resultWithCount.getCount());
                    return result;
                })
                .flatMap(searchResult ->
                        Flux.just(searchResult).flatMap(rs -> {
                                    switch (filter) {
                                        case CREATE_ASC:
                                            return this.beerRepository.findByGroupIdASC(query.getGroup_id(), "createat", page, size);
                                        case CREATE_DESC:
                                            return this.beerRepository.findByGroupIdDESC(query.getGroup_id(), "createat", page, size);
                                        case NAME_ASC:
                                            return this.beerRepository.findByGroupIdASC(query.getGroup_id(), "name", page, size);
                                        case NAME_DESC:
                                            return this.beerRepository.findByGroupIdDESC(query.getGroup_id(), "name", page, size);
                                        case PRICE_ASC:
                                            return this.beerRepository.adminGetAllBeerSortByPriceASC(query.getGroup_id(), page, size);
                                        case PRICE_DESC:
                                            return this.beerRepository.adminGetAllBeerSortByPriceDESC(query.getGroup_id(), page, size);
                                        case SOLD_NUM:
                                            return this.beerRepository.adminGetAllBeerSortBySoldDESC(query.getGroup_id(), page, size);
                                        default:
                                            return this.beerRepository.findByGroupIdDESC(query.getGroup_id(), "name", page, size);
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
        return this.resultWithCountRepository.countAll(query.getGroup_id())
                .map(resultWithCount -> {
                    SearchResult<BeerSubmitData> result = new SearchResult<>();
                    result.setCount(resultWithCount.getCount());
                    return result;
                })
                .flatMap(searchResult ->
                        Flux.just(searchResult).flatMap(rs -> {
                            switch (filter) {
                                case CREATE_ASC:
                                    return this.beerRepository.findByIdNotNullASC(query.getGroup_id(), page, size, "createat");
                                case CREATE_DESC:
                                    return this.beerRepository.findByIdNotNullDESC(query.getGroup_id(), page, size,  "createat");
                                case NAME_ASC:
                                    return this.beerRepository.findByIdNotNullASC(query.getGroup_id(), page, size, "name");
                                case NAME_DESC:
                                    return this.beerRepository.findByIdNotNullDESC(query.getGroup_id(), page, size,  "name");
                                case PRICE_ASC:
                                    return this.beerRepository.getAllBeerSortByPriceASC(query.getGroup_id(), page, size);
                                case PRICE_DESC:
                                    return this.beerRepository.getAllBeerSortByPriceDESC(query.getGroup_id(), page, size);
                                case SOLD_NUM:
                                    return this.beerRepository.getAllBeerSortBySoldDESC(query.getGroup_id(), page, size);
                                default:
                                    return this.beerRepository.findByIdNotNull(query.getGroup_id(), page, size);
                            }
                        })
                                .flatMap(this::CoverToSubmitData)
                                .map(searchResult::Add)
                                .then(Mono.just(searchResult))
                );
    }

    private Mono<BeerSubmitData> CoverToSubmitData(Product product) {
        return Mono.just(BeerSubmitData.builder().build().FromBeer(product))
                .flatMap(beerSubmitData ->
                        Mono.just(new ArrayList<ProductUnit>())
                                .flatMap(beerUnits ->
                                        beerUnitRepository.findByBeerID(beerSubmitData.getGroup_id(), beerSubmitData.getBeerSecondID())
                                                .map(ProductUnit::CheckDiscount)
                                                .map(beerUnits::add)
                                                .then(
                                                        Mono.just(beerUnits)
                                                )
                                                .map(beerSubmitData::SetBeerUnit)
                                )
                )
                .flatMap(beerSubmitData ->
                        imageRepository.findByCategory(beerSubmitData.getGroup_id(), beerSubmitData.getBeerSecondID())
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
                        return this.resultWithCountRepository.adminCountSearchBeer(query.getGroup_id(), searchTxt)
                                .map(resultWithCount -> {
                                    SearchResult<BeerSubmitData> result = SearchResult.<BeerSubmitData>builder()
                                            .isNormalSearch(true)
                                            .searchTxt(searchTxt)
                                            .build();
                                    result.setCount(resultWithCount.getCount());
                                    return result;
                                });
                    String finalSearchTxt = "%" + searchTxt + "%";
                    return this.resultWithCountRepository.adminCountSearchBeerLike(query.getGroup_id(), finalSearchTxt)
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
                                                        return this.beerRepository.adminSearchBeerSortByCreateASC(query.getGroup_id(), searchResult.getSearchTxt(), page, size);
                                                    case CREATE_DESC:
                                                        return this.beerRepository.adminSearchBeerSortByCreateDESC(query.getGroup_id(), searchResult.getSearchTxt(), page, size);
                                                    case NAME_ASC:
                                                        return this.beerRepository.adminSearchBeerSortByNameASC(query.getGroup_id(), searchResult.getSearchTxt(), page, size);
                                                    case NAME_DESC:
                                                        return this.beerRepository.adminSearchBeerSortByNameDESC(query.getGroup_id(), searchResult.getSearchTxt(), page, size);
                                                    case PRICE_ASC:
                                                        return this.beerRepository.adminSearchBeerSortByPriceASC(query.getGroup_id(), searchResult.getSearchTxt(), page, size);
                                                    case PRICE_DESC:
                                                        return this.beerRepository.adminSearchBeerSortByPriceDESC(query.getGroup_id(), searchResult.getSearchTxt(), page, size);
                                                    case SOLD_NUM:
                                                        return this.beerRepository.adminSearchBeerSortBySoldDESC(query.getGroup_id(), searchResult.getSearchTxt(), page, size);
                                                    default:
                                                        return this.beerRepository.adminSearchBeer(query.getGroup_id(), searchResult.getSearchTxt(), page, size);
                                                }
                                            }
                                            switch (filter){
                                                case CREATE_ASC:
                                                    return this.beerRepository.adminSearchBeerLikeSortCreateASC(query.getGroup_id(), searchResult.getSearchTxt(), page, size);
                                                case CREATE_DESC:
                                                    return this.beerRepository.adminSearchBeerLikeSortCreateDESC(query.getGroup_id(), searchResult.getSearchTxt(), page, size);
                                                case NAME_ASC:
                                                    return this.beerRepository.adminSearchBeerLikeSortByNameASC(query.getGroup_id(), searchResult.getSearchTxt(), page, size);
                                                case NAME_DESC:
                                                    return this.beerRepository.adminSearchBeerLikeSortByNameDESC(query.getGroup_id(), searchResult.getSearchTxt(), page, size);
                                                case PRICE_ASC:
                                                    return this.beerRepository.adminSearchBeerLikeSortByPriceASC(query.getGroup_id(), searchResult.getSearchTxt(), page, size);
                                                case PRICE_DESC:
                                                    return this.beerRepository.adminSearchBeerLikeSortByPriceDESC(query.getGroup_id(), searchResult.getSearchTxt(), page, size);
                                                case SOLD_NUM:
                                                    return this.beerRepository.adminSearchBeerLikeSortBySoldDESC(query.getGroup_id(), searchResult.getSearchTxt(), page, size);
                                                default:
                                                    return this.beerRepository.adminSearchBeerLike(query.getGroup_id(), searchResult.getSearchTxt(), page, size);
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
                        return this.resultWithCountRepository.countSearchBeer(query.getGroup_id(), searchTxt)
                                .map(resultWithCount -> {
                                    SearchResult<BeerSubmitData> result = SearchResult.<BeerSubmitData>builder()
                                            .isNormalSearch(true)
                                            .searchTxt(searchTxt)
                                            .build();
                                    result.setCount(resultWithCount.getCount());
                                    return result;
                                });
                    String finalSearchTxt = "%" + searchTxt + "%";
                    return this.resultWithCountRepository.countSearchBeerLike(query.getGroup_id(), finalSearchTxt)
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
                                                return this.beerRepository.searchBeerSortByCreateASC(query.getGroup_id(), searchResult.getSearchTxt(), page, size);
                                            case CREATE_DESC:
                                                return this.beerRepository.searchBeerSortByCreateDESC(query.getGroup_id(), searchResult.getSearchTxt(), page, size);
                                            case NAME_ASC:
                                                return this.beerRepository.searchBeerSortByNameASC(query.getGroup_id(), searchResult.getSearchTxt(), page, size);
                                            case NAME_DESC:
                                                return this.beerRepository.searchBeerSortByNameDESC(query.getGroup_id(), searchResult.getSearchTxt(), page, size);
                                            case PRICE_ASC:
                                                return this.beerRepository.searchBeerSortByPriceASC(query.getGroup_id(), searchResult.getSearchTxt(), page, size);
                                            case PRICE_DESC:
                                                return this.beerRepository.searchBeerSortByPriceDESC(query.getGroup_id(), searchResult.getSearchTxt(), page, size);
                                            case SOLD_NUM:
                                                return this.beerRepository.searchBeerSortBySoldDESC(query.getGroup_id(), searchResult.getSearchTxt(), page, size);
                                            default:
                                                return this.beerRepository.searchBeer(query.getGroup_id(), searchResult.getSearchTxt(), page, size);
                                        }
                                    }
                                    switch (filter){
                                        case CREATE_ASC:
                                            return this.beerRepository.searchBeerLikeSortCreateASC(query.getGroup_id(), searchResult.getSearchTxt(), page, size);
                                        case CREATE_DESC:
                                            return this.beerRepository.searchBeerLikeSortCreateDESC(query.getGroup_id(), searchResult.getSearchTxt(), page, size);
                                        case NAME_ASC:
                                            return this.beerRepository.searchBeerLikeSortByNameASC(query.getGroup_id(), searchResult.getSearchTxt(), page, size);
                                        case NAME_DESC:
                                            return this.beerRepository.searchBeerLikeSortByNameDESC(query.getGroup_id(), searchResult.getSearchTxt(), page, size);
                                        case PRICE_ASC:
                                            return this.beerRepository.searchBeerLikeSortByPriceASC(query.getGroup_id(), searchResult.getSearchTxt(), page, size);
                                        case PRICE_DESC:
                                            return this.beerRepository.searchBeerLikeSortByPriceDESC(query.getGroup_id(), searchResult.getSearchTxt(), page, size);
                                        case SOLD_NUM:
                                            return this.beerRepository.searchBeerLikeSortBySoldDESC(query.getGroup_id(), searchResult.getSearchTxt(), page, size);
                                        default:
                                            return this.beerRepository.searchBeerLike(query.getGroup_id(), searchResult.getSearchTxt(), page, size);
                                    }
                                })
                                .flatMap(this::CoverToSubmitData)
                                .map(searchResult::Add)
                                .then(Mono.just(searchResult))
                );
    }

    public Mono<SearchResult<BeerSubmitData>> AdminCountGetAllBeerByCategory(SearchQuery query) {
        final String category = query.getQuery();
        final SearchQuery.Filter filter = query.GetFilter();
        final int page = query.getPage();
        final int size = query.getSize();
        return this.resultWithCountRepository.AdminCountCategory(query.getGroup_id(), category)
                .map(resultWithCount -> {
                    SearchResult<BeerSubmitData> result = new SearchResult<>();
                    result.setCount(resultWithCount.getCount());
                    return result;
                })
                .flatMap(searchResult ->
                        Flux.just(searchResult).flatMap(rs -> {
                                    switch (filter) {
                                        case CREATE_ASC:
                                            return this.beerRepository.findByGroupIDAndCategory(query.getGroup_id(), category, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createat")));// page, size, Sort.Direction.ASC.name(), "createat");
                                        case CREATE_DESC:
                                            return this.beerRepository.findByGroupIDAndCategory(query.getGroup_id(), category, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createat")));// page, size, Sort.Direction.DESC.name(), "createat");
                                        case NAME_ASC:
                                            return this.beerRepository.findByGroupIDAndCategory(query.getGroup_id(), category, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name")));//page, size, Sort.Direction.ASC.name(), "name");
                                        case NAME_DESC:
                                            return this.beerRepository.findByGroupIDAndCategory(query.getGroup_id(), category, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "name")));//page, size, Sort.Direction.DESC.name(), "name");
                                        case PRICE_ASC:
                                            return this.beerRepository.AdminFindByCategoryBeerSortByPriceASC(query.getGroup_id(), category, page, size);
                                        case PRICE_DESC:
                                            return this.beerRepository.AdminFindByCategoryBeerSortByPriceDESC(query.getGroup_id(), category, page, size);
                                        case SOLD_NUM:
                                            return this.beerRepository.AdminFindByCategoryBeerSortBySoldDESC(query.getGroup_id(), category, page, size);
                                        default:
                                            return this.beerRepository.findByGroupIDAndCategory(query.getGroup_id(), category, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "name")));//page, size, Sort.Direction.DESC.name(), "name");
                                    }
                                })
                                .flatMap(this::CoverToSubmitData)
                                .map(searchResult::Add)
                                .then(Mono.just(searchResult))
                );
    }

    public Mono<SearchResult<BeerSubmitData>> CountGetAllBeerByCategory(SearchQuery query) {
        final String category = query.getQuery();
        final SearchQuery.Filter filter = query.GetFilter();
        final int page = query.getPage();
        final int size = query.getSize();
        return this.resultWithCountRepository.countCategory(query.getGroup_id(), category)
                .map(resultWithCount -> {
                    SearchResult<BeerSubmitData> result = new SearchResult<>();
                    result.setCount(resultWithCount.getCount());
                    return result;
                })
                .flatMap(searchResult ->
                        Flux.just(searchResult).flatMap(rs -> {
                            switch (filter) {
                                case CREATE_ASC:
                                    return this.beerRepository.findByCategoryASC(query.getGroup_id(), category, page, size, "createat");
                                case CREATE_DESC:
                                    return this.beerRepository.findByCategoryDESC(query.getGroup_id(), category, page, size, "createat");
                                case NAME_ASC:
                                    return this.beerRepository.findByCategoryASC(query.getGroup_id(), category, page, size, "name");
                                case NAME_DESC:
                                    return this.beerRepository.findByCategoryDESC(query.getGroup_id(), category, page, size, "name");
                                case PRICE_ASC:
                                    return this.beerRepository.findByCategoryBeerSortByPriceASC(query.getGroup_id(), category, page, size);
                                case PRICE_DESC:
                                    return this.beerRepository.findByCategoryBeerSortByPriceDESC(query.getGroup_id(), category, page, size);
                                case SOLD_NUM:
                                    return this.beerRepository.findByCategoryBeerSortBySoldDESC(query.getGroup_id(), category, page, size);
                                default:
                                    return this.beerRepository.findByCategoryDESC(query.getGroup_id(), category, page, size, "createat");
                            }
                        })
                                .flatMap(this::CoverToSubmitData)
                                .map(searchResult::Add)
                                .then(Mono.just(searchResult))
                );
    }
}
