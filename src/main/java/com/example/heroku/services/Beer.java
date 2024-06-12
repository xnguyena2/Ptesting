package com.example.heroku.services;

import com.example.heroku.model.Product;
import com.example.heroku.model.ProductImport;
import com.example.heroku.model.ProductUnit;
import com.example.heroku.model.UserPackageDetail;
import com.example.heroku.model.repository.*;
import com.example.heroku.request.beer.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    JoinProductWithProductUnit joinProductWithProductUnit;

    @Autowired
    GroupImportRepository groupImportRepository;

    public Mono<BeerSubmitData> CreateBeer(@Valid @ModelAttribute BeerInfo info) {
        info.CorrectData();
        String actionID = Util.getInstance().GenerateID();
        return Mono.just(info.getProduct())
                .flatMap(product ->
                        this.beerRepository.saveProduct(product.getGroup_id(), product.getProduct_second_id(),
                                product.getName(), product.getDetail(),
                                product.getCategory(), product.getUnit_category_config(),
                                product.getMeta_search(), product.isVisible_web(),
                                product.getStatus(), product.getCreateat()
                        ).then(Mono.just(product))
                )
                .flatMap(product -> this.searchBeer.saveToken(product.getGroup_id(), product.getProduct_second_id(),
                                product.getTokens(), product.getCreateat())
                        .then(Mono.just(product))
                )
                .flatMap(product -> this.beerUnitRepository.setActionID(product.getGroup_id(), product.getProduct_second_id(), actionID, ProductImport.ImportType.DELETE_PRODUCT.getName())
                        .then(Mono.just(product))
                )
                .flatMap(product -> this.beerUnitRepository.deleteByBeerId(product.getGroup_id(), product.getProduct_second_id()))
                .thenMany(Flux.just(info.getProductUnitPrepareForInventory(actionID))
                        .flatMap(beerUnit -> this.beerUnitRepository.save(beerUnit))
                )
                .then(groupImportRepository.createGroupImportForEmpty(info.getProduct().getGroup_id(), actionID, ProductImport.ImportType.UPDATE_NUMBER.getName(), ProductImport.Status.DONE.getName()))
                .then(Mono.just(BeerSubmitData.FromProductInfo(info)));
    }

    public Mono<String> UpdateProductUnitWareHouseSetting(ProductUnitUpdate productUnitUpdate) {
        String actionID = Util.getInstance().GenerateID();
        return beerUnitRepository.updateInventoryAndEnableWarehouse(productUnitUpdate.getGroup_id(), productUnitUpdate.getProduct_second_id(), productUnitUpdate.getProduct_unit_second_id(), productUnitUpdate.getInventory_number(), productUnitUpdate.isEnable_warehouse(), productUnitUpdate.getStatus(),
                        actionID, ProductImport.ImportType.UPDATE_NUMBER.getName())
                .then(groupImportRepository.createGroupImportForEmpty(productUnitUpdate.getGroup_id(), actionID, ProductImport.ImportType.UPDATE_NUMBER.getName(), ProductImport.Status.DONE.getName()))
                .then(Mono.just(actionID));
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

    public Mono<Map<String, List<com.example.heroku.model.Image>>> GetListImgOfProductOfPackage(String group_id, UserPackageDetail.Status status, UserPackageDetail.Status or_status, int page, int size) {
        Map<String, List<com.example.heroku.model.Image>> imgMap = new HashMap<>();
        return Mono.just(imgMap).flatMap(stringListMap ->
                imageRepository.getAllOfProductByPackage(group_id, status, or_status, page, size)
                        .groupBy(com.example.heroku.model.Image::getCategory)
                        .flatMap(stringImageGroupedFlux ->
                                stringImageGroupedFlux.collectList().map(images -> {
                                    stringListMap.put(images.get(0).getCategory(), images);
                                    return 0;
                                })
                        ).then(Mono.just(stringListMap))
        );
    }

    public Mono<Map<String, List<com.example.heroku.model.Image>>> GetListImgOfProductOfPackageAfterID(String group_id, UserPackageDetail.Status status, UserPackageDetail.Status or_status, int id, int page, int size) {
        Map<String, List<com.example.heroku.model.Image>> imgMap = new HashMap<>();
        return Mono.just(imgMap).flatMap(stringListMap ->
                imageRepository.getAllOfProductByPackageAferID(group_id, status, or_status, id, page, size)
                        .groupBy(com.example.heroku.model.Image::getCategory)
                        .flatMap(stringImageGroupedFlux ->
                                stringImageGroupedFlux.collectList().map(images -> {
                                    stringListMap.put(images.get(0).getCategory(), images);
                                    return 0;
                                })
                        ).then(Mono.just(stringListMap))
        );
    }

    public Mono<BeerSubmitData> GetBeerByID(String  groupID, String id) {
        return beerRepository.findBySecondID(groupID, id)
                .flatMap(this::CoverToSubmitData);
    }

    public Mono<BeerSubmitData> GetBeerByIDWithUnit(String  groupID, String productID, String productUnitID) {
        return this.joinProductWithProductUnit.GetProductAndUnit(groupID, productID, productUnitID)
                .flatMap(this::_setImg4SubmitData);
    }

    public Mono<BeerSubmitData> GetBeerByIDWithUnit(String  groupID, String productID, String productUnitID, Map<String, List<com.example.heroku.model.Image>> mapImg) {
        return this.joinProductWithProductUnit.GetProductAndUnit(groupID, productID, productUnitID)
                .map(beerSubmitData -> beerSubmitData.SetImg(mapImg));
    }

    public Flux<BeerSubmitData> GetAllBeer(SearchQuery query) {
        return this.beerRepository.findByIdNotNull(query.getGroup_id(), query.getPage(), query.getSize())
                .flatMap(this::CoverToSubmitData);
    }

    public Flux<BeerSubmitData> GetAllBeerByJoinFirst(SearchQuery query) {
        Map<String, List<com.example.heroku.model.Image>> imgMap = new HashMap<>();
        return Mono.just(imgMap).flatMap(stringListMap ->
                imageRepository.getAllOfProduct(query.getGroup_id())
                        .groupBy(com.example.heroku.model.Image::getCategory)
                        .flatMap(stringImageGroupedFlux ->
                                stringImageGroupedFlux.collectList().map(images -> {
                                    stringListMap.put(images.get(0).getCategory(), images);
                                    return 0;
                                })
                        ).then(Mono.just(stringListMap))
        ).flatMapMany(mapMono ->
                this.joinProductWithProductUnit.GetAllBeerByJoinFirstNotHide(query)
                        .map(beerSubmitData -> beerSubmitData.SetImg(mapMono))
        );
    }

    public Flux<BeerSubmitData> GetAllBeerByJoinFirstForWeb(SearchQuery query) {
        return this.joinProductWithProductUnit.GetAllBeerByJoinFirstNotHideForWeb(query)
                .flatMap(this::_setImg4SubmitData);
    }

    public Flux<ProductUnit> getAllProductUnitOfIDs(String groupID, List<String> productIDs, List<String> productUinitIDs) {
        return beerUnitRepository.getListUnitByListIDs(groupID, productIDs, productUinitIDs);
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
        return Mono.just(BeerSubmitData.FromBeer(product))
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
                .flatMap(this::_setImg4SubmitData);
    }

    private Mono<BeerSubmitData> _setImg4SubmitData(BeerSubmitData beerSubmitData) {
        return imageRepository.findByCategory(beerSubmitData.getGroup_id(), beerSubmitData.getBeerSecondID())
                .map(beerSubmitData::AddImage)
                .then(Mono.just(beerSubmitData));
    }

    private Mono<BeerSubmitData> CoverToSubmitDataWithUnitID(Product product, String unitID) {
        return Mono.just(BeerSubmitData.FromBeer(product))
                .flatMap(beerSubmitData ->
                        Mono.just(new ArrayList<ProductUnit>())
                                .flatMap(beerUnits ->
                                        beerUnitRepository.findByBeerUnitID(beerSubmitData.getGroup_id(), beerSubmitData.getBeerSecondID(), unitID)
                                                .map(ProductUnit::CheckDiscount)
                                                .map(beerUnits::add)
                                                .then(
                                                        Mono.just(beerUnits)
                                                )
                                                .map(beerSubmitData::SetBeerUnit)
                                )
                )
                .flatMap(this::_setImg4SubmitData);
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
