package com.example.heroku.services;

import com.example.heroku.model.Image;
import com.example.heroku.model.ProductImport;
import com.example.heroku.model.UserPackageDetail;
import com.example.heroku.model.joinwith.UserPackageDetailJoinWithUserPackage;
import com.example.heroku.model.repository.GroupImportRepository;
import com.example.heroku.model.repository.JoinUserPackageDetailWithUserPackgeRepository;
import com.example.heroku.model.repository.UserPackageDetailRepository;
import com.example.heroku.model.repository.UserPackageRepository;
import com.example.heroku.request.beer.*;
import com.example.heroku.request.client.PackageID;
import com.example.heroku.request.client.UserID;
import com.example.heroku.request.client.UserPackageID;
import com.example.heroku.response.BuyerData;
import com.example.heroku.response.Format;
import com.example.heroku.response.PackageDataResponse;
import com.example.heroku.response.ProductInPackageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@Component
public class UserPackage {

    @Autowired
    com.example.heroku.services.Beer beerAPI;

    @Autowired
    com.example.heroku.services.Buyer buyer;

    @Autowired
    UserPackageRepository userPackageRepository;

    @Autowired
    UserPackageDetailRepository userPackageDetailRepository;

    @Autowired
    JoinUserPackageDetailWithUserPackgeRepository joinUserPackageDetailWithUserPackgeRepository;

    @Autowired
    GroupImportRepository groupImportRepository;

    public Mono<ResponseEntity<Format>> AddProductToPackage(ProductPackage productPackage) {

        if (productPackage.isEmpty())
            return Mono.just(badRequest().body(Format.builder().response("user package empty").build()));
        return
                Mono.just(productPackage)
                        .flatMap(this::saveBuyer)
                        .flatMap(this::savePackageDetail)
                        .flatMap(productPackage1 -> Flux.just(productPackage1.getUserPackage())
                                .flatMap(userPackage ->
                                        userPackageRepository.BuyerHimSelfAddPackage(userPackage.getGroup_id(), userPackage.getDevice_id(), userPackage.getPackage_second_id(), userPackage.getProduct_second_id(), userPackage.getProduct_unit_second_id(),
                                                userPackage.getProduct_name(), userPackage.getProduct_unit_name(), userPackage.getProduct_group_unit_name(),
                                                userPackage.getNumber_services_unit(),
                                                userPackage.getNumber_unit(), userPackage.getStatus(),
                                                userPackage.getDepend_to_product())

                                )
                                .then(groupImportRepository.createGroupImportForEmpty(productPackage.getGroup_id(), productPackage.getPackage_second_id(), ProductImport.ImportType.SELLING.getName(), ProductImport.Status.DONE.getName()))
                                .then(Mono.just(ok(Format.builder().response("done").build())))
                        );
    }

    @Deprecated
    public Mono<ResponseEntity<Format>> SavePackage(ProductPackage productPackage) {

        if (productPackage.isEmpty())
            return Mono.just(badRequest().body(Format.builder().response("user package empty").build()));
        return
                userPackageDetailRepository.GetPackageDetailByID(productPackage.getGroup_id(), productPackage.getPackage_second_id())
                        .switchIfEmpty(Mono.just(UserPackageDetail.builder().status(UserPackageDetail.Status.CREATE).build()))
                        .filter(userPackageDetail -> userPackageDetail.getStatus() != UserPackageDetail.Status.DONE)
                        .flatMap(userPackageDetail -> saveBuyer(productPackage))
                        .flatMap(this::savePackageDetail)
                        .flatMap(productPackage1 -> userPackageRepository.DeleteProductByPackageID(productPackage1.getGroup_id(), productPackage1.getPackage_second_id())
                                .then(Mono.just(productPackage1))
                        )
                        .filter(productPackage1 -> !productPackage1.isEmpty())
                        .flatMapMany(productPackage1 -> Flux.just(productPackage1.getUserPackage()))
                        .flatMap(this::savePackageItem)
                        .then(groupImportRepository.createGroupImportForEmpty(productPackage.getGroup_id(), productPackage.getPackage_second_id(),
                                productPackage.getStatus() == UserPackageDetail.Status.CANCEL ? ProductImport.ImportType.SELLING_RETURN.getName() : ProductImport.ImportType.SELLING.getName(),
                                productPackage.getStatus() == UserPackageDetail.Status.CANCEL ? ProductImport.Status.RETURN.getName() : ProductImport.Status.DONE.getName()
                        ))
                        .then(Mono.just(ok(Format.builder().response("done").build())));
    }

    public Mono<ResponseEntity<Format>> SavePackageWithoutCheckWithTransaction(ProductPackgeWithTransaction productPackgeWithTransaction) {
        return SavePackageWithoutCheck(productPackgeWithTransaction.getProductPackage());
    }

    public Mono<ResponseEntity<Format>> SavePackageWithoutCheck(ProductPackage productPackage) {

        if (productPackage.isEmpty())
            return Mono.just(badRequest().body(Format.builder().response("user package empty").build()));
        return
                Mono.just(productPackage)
                        .flatMap(this::saveBuyer)
                        .flatMap(this::savePackageDetail)
                        .flatMap(productPackage1 -> userPackageRepository.DeleteProductByPackageID(productPackage1.getGroup_id(), productPackage1.getPackage_second_id())
                                .then(Mono.just(productPackage1))
                        )
                        .filter(productPackage1 -> !productPackage1.isEmpty())
                        .flatMapMany(productPackage1 -> Flux.just(productPackage1.getUserPackage()))
                        .flatMap(this::savePackageItemWithoutCheck)
                        .then(groupImportRepository.createGroupImportForEmpty(productPackage.getGroup_id(), productPackage.getPackage_second_id(),
                                productPackage.getStatus() == UserPackageDetail.Status.CANCEL || productPackage.getStatus() == UserPackageDetail.Status.RETURN ? ProductImport.ImportType.SELLING_RETURN.getName() : ProductImport.ImportType.SELLING.getName(),
                                productPackage.getStatus() == UserPackageDetail.Status.CANCEL || productPackage.getStatus() == UserPackageDetail.Status.RETURN ? ProductImport.Status.RETURN.getName() : ProductImport.Status.DONE.getName()
                        ))
                        .then(Mono.just(ok(Format.builder().response("done").build())));
    }

    Mono<ProductPackage> savePackageDetail(ProductPackage productPackage) {
        return savePackageDetailToRepo(productPackage.AutoFill())
                .then(Mono.just(productPackage));
    }

    public Mono<ResponseEntity<Format>> SavePackageDetailKitchen(ProductPackage productPackage) {
        return savePackageDetailKitchenToRepo(productPackage.AutoFill())
                .then(Mono.just(ok(Format.builder().response("done").build())));
    }

    Mono<ProductPackage> saveBuyer(ProductPackage productPackage) {

        com.example.heroku.model.Buyer buyerInfo = productPackage.getBuyer();
        if (buyerInfo == null && productPackage.getStatus() == UserPackageDetail.Status.DONE) {
            buyerInfo = com.example.heroku.model.Buyer.CreateUnknowBuyer();
        }
        if (buyerInfo == null) {
            return Mono.just(productPackage);
        }
        buyerInfo.AutoFill(productPackage.getGroup_id());
        productPackage.setDevice_id(buyerInfo.getDevice_id());
        float totalPrice = productPackage.getStatus() == UserPackageDetail.Status.DONE ? productPackage.getPrice() : 0;
        float realPrice = productPackage.getStatus() == UserPackageDetail.Status.DONE ? productPackage.getPayment() : 0;
        float discount = productPackage.getStatus() == UserPackageDetail.Status.DONE ? productPackage.GetDiscountValue() : 0;
        float ship = productPackage.getStatus() == UserPackageDetail.Status.DONE ? productPackage.getShip_price() : 0;
        int point = productPackage.getStatus() == UserPackageDetail.Status.DONE ? productPackage.getPoint() : 0;
        return buyer.insertOrUpdate(buyerInfo, totalPrice, realPrice, ship, discount, point).then(Mono.just(productPackage));
    }

    Mono<UserPackageDetail> removePackgeOfBuyer(UserPackageDetail productPackage) {

        String device_id = productPackage.getDevice_id();
        if (device_id != null && productPackage.getStatus() == UserPackageDetail.Status.DONE) {
            float totalPrice = -productPackage.getPrice();
            float realPrice = -productPackage.getPayment();
            float discount = -productPackage.GetDiscountValue();
            float ship = -productPackage.getShip_price();
            int point = -productPackage.getPoint();
            return buyer.updatePrice(productPackage.getGroup_id(), device_id, totalPrice, realPrice, ship, discount, point).then(Mono.just(productPackage));
        }
        return Mono.just(productPackage);
    }

    Mono<UserPackageDetail> savePackageDetailToRepo(UserPackageDetail detail) {
        return userPackageDetailRepository.InsertOrUpdate(detail.getGroup_id(), detail.getDevice_id(), detail.getStaff_id(), detail.getStaff_name(), detail.getPackage_second_id(),
                        detail.getPackage_type(), detail.getVoucher(),
                        detail.getArea_id(), detail.getArea_name(), detail.getTable_id(), detail.getTable_name(),
                        detail.getPrice(), detail.getPayment(), detail.getDiscount_amount(), detail.getDiscount_percent(),
                        detail.getDiscount_promotional(), detail.getDiscount_by_point(), detail.getAdditional_fee(), detail.getAdditional_config(),
                        detail.getShip_price(), detail.getDeliver_ship_price(), detail.getCost(), detail.getProfit(),
                        detail.getPoint(), detail.getNote(), detail.getImage(), detail.getProgress(), detail.getMeta_search(),
                        detail.getMoney_source(), detail.getPrint_kitchen(),
                        detail.getStatus(), detail.getCreateat())
                .then(Mono.just(detail));
    }

    Mono<UserPackageDetail> savePackageDetailKitchenToRepo(UserPackageDetail detail) {
        return userPackageDetailRepository.SavePrintKitchen(detail.getGroup_id(), detail.getDevice_id(), detail.getPrint_kitchen())
                .then(Mono.just(detail));
    }

    Mono<com.example.heroku.model.UserPackage> savePackageItem(com.example.heroku.model.UserPackage userPackage) {
        return userPackageRepository.InsertOrUpdatePackage(userPackage.getGroup_id(), userPackage.getDevice_id(), userPackage.getPackage_second_id(), userPackage.getProduct_second_id(), userPackage.getProduct_unit_second_id(),
                userPackage.getProduct_name(), userPackage.getProduct_unit_name(), userPackage.getProduct_group_unit_name(),
                userPackage.getProduct_type(), userPackage.getNumber_services_unit(),
                userPackage.getNumber_unit(), userPackage.getBuy_price(), userPackage.getPrice(),
                userPackage.getDiscount_amount(), userPackage.getDiscount_percent(), userPackage.getDiscount_promotional(),
                userPackage.getNote(), userPackage.getStatus(), userPackage.getDepend_to_product(), userPackage.getCreateat());
    }

    Mono<com.example.heroku.model.UserPackage> savePackageItemWithoutCheck(com.example.heroku.model.UserPackage userPackage) {
        return userPackageRepository.InsertOrUpdatePackageWithoutCheck(userPackage.getGroup_id(), userPackage.getDevice_id(), userPackage.getPackage_second_id(), userPackage.getProduct_second_id(), userPackage.getProduct_unit_second_id(),
                userPackage.getProduct_name(), userPackage.getProduct_unit_name(), userPackage.getProduct_group_unit_name(),
                userPackage.getProduct_type(), userPackage.getNumber_services_unit(),
                userPackage.getNumber_unit(), userPackage.getBuy_price(), userPackage.getPrice(),
                userPackage.getDiscount_amount(), userPackage.getDiscount_percent(), userPackage.getDiscount_promotional(),
                userPackage.getNote(), userPackage.getStatus(), userPackage.getDepend_to_product(), userPackage.getCreateat());
    }

    Mono<PackageDataResponse> fillPackageItem(PackageDataResponse packageDataResponse) {
        return
                userPackageRepository.GetDevicePackageWithID(packageDataResponse.getGroup_id(), packageDataResponse.getPackage_second_id())
                        .map(ProductInPackageResponse::new)
                        .map(packageDataResponse::addItem)
                        .then(
                                beerAPI.GetAllProductOfPackage(packageDataResponse.getGroup_id(), packageDataResponse.getPackage_second_id())
                                        .collectList().map(packageDataResponse::setProductData)
                        )
                        .then(buyer.FindByDeviceID(packageDataResponse.getGroup_id(), packageDataResponse.getDevice_id())
                                .handle((buyerData, synchronousSink) -> synchronousSink.next(packageDataResponse.setBuyer(buyerData)))
                        )
                        .then(Mono.just(packageDataResponse));
    }

    Mono<PackageDataResponse> fillProductAndBuyer(PackageDataResponse packageDataResponse) {
        return
                beerAPI.GetAllProductOfPackage(packageDataResponse.getGroup_id(), packageDataResponse.getPackage_second_id())
                        .collectList().map(packageDataResponse::setProductData)
                        .then(buyer.FindByDeviceID(packageDataResponse.getGroup_id(), packageDataResponse.getDevice_id())
                                .handle((buyerData, synchronousSink) -> synchronousSink.next(packageDataResponse.setBuyer(buyerData)))
                        )
                        .then(Mono.just(packageDataResponse));
    }

    Mono<PackageDataResponse> fillProductAndBuyer(PackageDataResponse packageDataResponse, Map<String, List<Image>> mapImg) {
        return
                beerAPI.GetAllProductOfPackage(packageDataResponse.getGroup_id(), packageDataResponse.getPackage_second_id(), mapImg)
                        .collectList().map(packageDataResponse::setProductData)
                        .then(buyer.FindByDeviceID(packageDataResponse.getGroup_id(), packageDataResponse.getDevice_id())
                                .handle((buyerData, synchronousSink) -> synchronousSink.next(packageDataResponse.setBuyer(buyerData)))
                        )
                        .then(Mono.just(packageDataResponse));
    }


    public Flux<PackageDataResponse> GetMyPackage(UserID userID) {
        return userPackageDetailRepository.GetDevicePackageDetail(userID.getGroup_id(), userID.getId(), userID.getPage(), userID.getSize())
                .map(PackageDataResponse::new)
                .flatMap(this::fillPackageItem);
    }

    public Flux<PackageDataResponse> GetMyPackageWorking(UserID userID) {
        long id = userID.getAfter_id();
        if (id > 0) {
            return GetPackageWorkingOfDeviceAfterID(userID);
        }
        return GetPackageWorkingOfDevice(userID);
    }


    private Flux<PackageDataResponse> GetPackageWorkingOfDevice(UserID userID) {
        return userPackageDetailRepository.GetDevicePackageDetailStatus(userID.getGroup_id(), userID.getId(), UserPackageDetail.Status.DONE, UserPackageDetail.Status.CREATE, userID.getPage(), userID.getSize())
                .map(PackageDataResponse::new)
                .flatMap(this::fillPackageItem);
    }


    private Flux<PackageDataResponse> GetPackageWorkingOfDeviceAfterID(UserID userID) {
        return userPackageDetailRepository.GetDevicePackageDetailStatusAfterID(userID.getGroup_id(), userID.getId(), UserPackageDetail.Status.DONE, UserPackageDetail.Status.CREATE, userID.getAfter_id(), userID.getPage(), userID.getSize())
                .map(PackageDataResponse::new)
                .flatMap(this::fillPackageItem);
    }


    public Flux<PackageDataResponse> GetMyPackageOfStatus(UserID userID, UserPackageDetail.Status status) {
        return userPackageDetailRepository.GetDevicePackageDetailByStatus(userID.getGroup_id(), userID.getId(), status, userID.getPage(), userID.getSize())
                .map(PackageDataResponse::new)
                .flatMap(this::fillPackageItem);
    }


    public Flux<PackageDataResponse> GetMyPackageOfStatusAfterID(UserID userID, UserPackageDetail.Status status) {
        return userPackageDetailRepository.GetDevicePackageDetailByStatusAfterID(userID.getGroup_id(), userID.getId(), status, userID.getAfter_id(), userID.getPage(), userID.getSize())
                .map(PackageDataResponse::new)
                .flatMap(this::fillPackageItem);
    }

    public Flux<PackageDataResponse> GetPackageByGroup(UserID userID) {
        return userPackageDetailRepository.GetAllPackageDetail(userID.getGroup_id(), userID.getPage(), userID.getSize())
                .map(PackageDataResponse::new)
                .flatMap(this::fillPackageItem);
    }

    public Flux<PackageDataResponse> GetPackageByGroupOnlyBuyerData(UserID userID) {
        if (userID.getAfter_id() == 0){
            return GetPackageByGroupAndOnlyBuyer(userID);
        }
        return GetPackageByGroupAndOnlyBuyerAfterID(userID);
    }

    private Flux<PackageDataResponse> GetPackageByGroupAndOnlyBuyer(UserID userID) {
        return userPackageDetailRepository.GetAllPackageDetail(userID.getGroup_id(), userID.getPage(), userID.getSize())
                .map(PackageDataResponse::new)
                .flatMap(packageDataResponse -> {
                    if (packageDataResponse.getDevice_id() == null || packageDataResponse.getDevice_id().isEmpty()) {
                        return Mono.just(packageDataResponse);
                    }
                    return buyer.FindByDeviceID(packageDataResponse.getGroup_id(), packageDataResponse.getDevice_id())
                            .switchIfEmpty(Mono.just(new BuyerData()))
                            .map(packageDataResponse::setBuyer);
                        }
                );
    }

    private Flux<PackageDataResponse> GetPackageByGroupAndOnlyBuyerAfterID(UserID userID) {
        long id = userID.getAfter_id();
        return userPackageDetailRepository.GetAllPackageDetailAfterID(userID.getGroup_id(), id, userID.getPage(), userID.getSize())
                .map(PackageDataResponse::new)
                .flatMap(packageDataResponse -> {
                            if (packageDataResponse.getDevice_id() == null || packageDataResponse.getDevice_id().isEmpty()) {
                                return Mono.just(packageDataResponse);
                            }
                            return buyer.FindByDeviceID(packageDataResponse.getGroup_id(), packageDataResponse.getDevice_id())
                                    .switchIfEmpty(Mono.just(new BuyerData()))
                                    .map(packageDataResponse::setBuyer);
                        }
                );
    }

    @Deprecated()
    public Flux<PackageDataResponse> GetWorkingPackageByGroup(UserID userID) {
        if (userID.getId() == null || userID.getId().isEmpty())
            return GetPackageByGrouAndStatus(userID, UserPackageDetail.Status.CREATE, UserPackageDetail.Status.DONE);
        return GetWorkingPackageByGroupAfterID(userID);
    }

    public Flux<PackageDataResponse> GetWorkingPackageByGroupByJoinWith(UserID userID) {
        if (userID.getId() == null || userID.getId().isEmpty())
            return GetPackageJoinByGrouAndStatus(userID, UserPackageDetail.Status.CREATE, UserPackageDetail.Status.DONE);
        return GetPackageJoinByGrouAndStatusAfterID(userID, UserPackageDetail.Status.CREATE, UserPackageDetail.Status.DONE);
    }

    public Flux<PackageDataResponse> SearchWorkingPackageByGroupByJoinWith(SearchQuery searchQuery) {
        searchQuery.setQuery("%" + searchQuery.getQuery() + "%");
        if (searchQuery.GetFilterTxt() == null || searchQuery.GetFilterTxt().isEmpty())
            return SearchPackageJoinByGrouAndStatus(searchQuery, UserPackageDetail.Status.CREATE, UserPackageDetail.Status.DONE);
        return SearchPackageJoinByGrouAndStatusAfterID(searchQuery, UserPackageDetail.Status.CREATE, UserPackageDetail.Status.DONE);
    }

    @Deprecated
    public Flux<PackageDataResponse> GetWorkingPackageByGroupAfterID(UserID userID) {
        return GetPackageByGrouAndStatusAfterID(userID, UserPackageDetail.Status.CREATE, UserPackageDetail.Status.DONE);
    }

    public Flux<PackageDataResponse> GetPackageByGroupAndStatus(UserID userID, UserPackageDetail.Status status) {
        if (userID.getId() == null || userID.getId().isEmpty())
            return GetPackageByGrouAndStatus(userID, status);
        return GetPackageByGrouAndStatusAfterID(userID, status);
    }

    public Flux<PackageDataResponse> GetPackageByGrouAndStatus(UserID userID, UserPackageDetail.Status status) {
        return userPackageDetailRepository.GetAllPackageDetailByStatus(userID.getGroup_id(), status, userID.getPage(), userID.getSize())
                .map(PackageDataResponse::new)
                .flatMap(this::fillPackageItem);
    }

    public Flux<PackageDataResponse> GetPackageByGrouAndStatusAfterID(UserID userID, UserPackageDetail.Status status) {
        int id = Integer.parseInt(userID.getId());
        return userPackageDetailRepository.GetAllPackageDetailByStatusAfterID(userID.getGroup_id(), status, id, userID.getPage(), userID.getSize())
                .map(PackageDataResponse::new)
                .flatMap(this::fillPackageItem);
    }

    @Deprecated
    public Flux<PackageDataResponse> GetPackageByGrouAndStatus(UserID userID, UserPackageDetail.Status status, UserPackageDetail.Status or_status) {
        return userPackageDetailRepository.GetAllPackageDetailByStatus(userID.getGroup_id(), status, or_status, userID.getPage(), userID.getSize())
                .map(PackageDataResponse::new)
                .flatMap(this::fillPackageItem);
    }

    public Flux<PackageDataResponse> GetPackageJoinByGrouAndStatus(UserID userID, UserPackageDetail.Status status, UserPackageDetail.Status or_status) {
        return beerAPI.GetListImgOfProductOfPackage(userID.getGroup_id(), status, or_status, userID.getPage(), userID.getSize())
                .flatMapMany(stringListMap ->
                        joinUserPackageDetailWithUserPackgeRepository.getUserPackgeDetailAndPackageItem(userID.getGroup_id(), status, or_status, userID.getPage(), userID.getSize())
                                .groupBy(UserPackageDetailJoinWithUserPackage::getPackage_second_id)
                                .flatMap(groupedFlux -> groupedFlux.collectList().map(UserPackageDetailJoinWithUserPackage::GeneratePackageData))
                                .flatMap(packageDataResponse -> fillProductAndBuyer(packageDataResponse, stringListMap))
                );
    }

    public Flux<PackageDataResponse> GetPackageJoinByGrouAndStatusAfterID(UserID userID, UserPackageDetail.Status status, UserPackageDetail.Status or_status) {
        long id = Long.parseLong(userID.getId());
        return beerAPI.GetListImgOfProductOfPackageAfterID(userID.getGroup_id(), status, or_status, id, userID.getPage(), userID.getSize())
                .flatMapMany(stringListMap ->
                        joinUserPackageDetailWithUserPackgeRepository.getUserPackgeDetailAndPackageItemAferID(userID.getGroup_id(), status, or_status, id, userID.getPage(), userID.getSize())
                                .groupBy(UserPackageDetailJoinWithUserPackage::getPackage_second_id)
                                .flatMap(groupedFlux -> groupedFlux.collectList().map(UserPackageDetailJoinWithUserPackage::GeneratePackageData))
                                .flatMap(packageDataResponse -> fillProductAndBuyer(packageDataResponse, stringListMap))
                );
    }

    public Flux<PackageDataResponse> SearchPackageJoinByGrouAndStatus(SearchQuery searchQuery, UserPackageDetail.Status status, UserPackageDetail.Status or_status) {
        return joinUserPackageDetailWithUserPackgeRepository.searchUserPackgeDetailAndPackageItem(searchQuery.getGroup_id(), searchQuery.getQuery(), status, or_status, searchQuery.getPage(), searchQuery.getSize())
                .groupBy(UserPackageDetailJoinWithUserPackage::getPackage_second_id)
                .flatMap(groupedFlux -> groupedFlux.collectList().map(UserPackageDetailJoinWithUserPackage::GeneratePackageData))
                .flatMap(this::fillProductAndBuyer);
    }

    public Flux<PackageDataResponse> SearchPackageJoinByGrouAndStatusAfterID(SearchQuery searchQuery, UserPackageDetail.Status status, UserPackageDetail.Status or_status) {
        int id = Integer.parseInt(searchQuery.GetFilterTxt());
        return joinUserPackageDetailWithUserPackgeRepository.searchUserPackgeDetailAndPackageItemAferID(searchQuery.getGroup_id(), searchQuery.getQuery(), status, or_status, id, searchQuery.getPage(), searchQuery.getSize())
                .groupBy(UserPackageDetailJoinWithUserPackage::getPackage_second_id)
                .flatMap(groupedFlux -> groupedFlux.collectList().map(UserPackageDetailJoinWithUserPackage::GeneratePackageData))
                .flatMap(this::fillProductAndBuyer);
    }

    public Flux<PackageDataResponse> GetPackageByGrouAndStatusAfterID(UserID userID, UserPackageDetail.Status status, UserPackageDetail.Status or_status) {
        int id = Integer.parseInt(userID.getId());
        return userPackageDetailRepository.GetAllPackageDetailByStatusAfterID(userID.getGroup_id(), status, or_status, id, userID.getPage(), userID.getSize())
                .map(PackageDataResponse::new)
                .flatMap(this::fillPackageItem);
    }

    public Mono<PackageDataResponse> GetPackage(PackageID packageID) {

        return userPackageDetailRepository.GetPackageDetail(packageID.getGroup_id(), packageID.getDevice_id(), packageID.getPackage_id())
                .map(PackageDataResponse::new)
                .flatMap(this::fillPackageItem);
    }

    public Mono<PackageDataResponse> GetJustByPackageId(PackageID packageID) {
        return joinUserPackageDetailWithUserPackgeRepository.getUserPackageDetailByID(packageID.getGroup_id(), packageID.getPackage_id())
                .collectList().map(UserPackageDetailJoinWithUserPackage::GeneratePackageData)
                .filter(packageDataResponse -> packageDataResponse.getPackage_second_id() != null)
                .flatMap(this::fillProductAndBuyer);
    }

    private Mono<ResponseEntity<Format>> ReturnOrCalcelPackage(PackageID packageID, UserPackageDetail.Status status) {

        return userPackageDetailRepository.GetPackageDetailByID(packageID.getGroup_id(), packageID.getPackage_id())
                .filter(sd -> sd.getStatus() == UserPackageDetail.Status.CREATE && status == UserPackageDetail.Status.CANCEL ||
                        sd.getStatus() == UserPackageDetail.Status.DONE && status == UserPackageDetail.Status.RETURN)
                .flatMap(userPackageDetail -> userPackageRepository.UpdateStatusByPackgeID(packageID.getGroup_id(), packageID.getPackage_id(), status)
                        .then(
                                userPackageDetailRepository.UpdateStatusByID(packageID.getGroup_id(), packageID.getPackage_id(), status)
                        )
                        .thenMany(removePackgeOfBuyer(userPackageDetail))
                        .then(Mono.just(ok(Format.builder().response("done").build())))
                );
    }

    public Mono<ResponseEntity<Format>> BuyerFromWebSubmitPackage(ProductPackage productPackage) {

        return userPackageDetailRepository.GetPackageDetailByID(productPackage.getGroup_id(), productPackage.getPackage_second_id())
                .filter(sd -> sd.getStatus() == UserPackageDetail.Status.WEB_TEMP)
                .flatMap(ProductPackage -> {
                    com.example.heroku.model.Buyer buyer1 = productPackage.getBuyer();
                    if (buyer1 == null) {
                        return Mono.just(ProductPackage);
                    }
                    buyer1.AutoFill(productPackage.getGroup_id());
                    String phone = buyer1.getPhone_number_clean();
                    System.out.println("web submit order by phone : " + phone);
                    return buyer.FindByPhoneClean(SearchQuery.builder().group_id(productPackage.getGroup_id()).query(phone).build())
                            .map(buyerData -> {
                                System.out.println("Found buyer id: " + buyerData.getDevice_id());
                                buyer1.setDevice_id(buyerData.getDevice_id());
                                return ProductPackage;
                            });
                })
                .then(Mono.just(productPackage.SetProductPackageForWebSubmit()))
                .flatMap(this::SavePackageWithoutCheck);
    }

    public Mono<ResponseEntity<Format>> ReturnPackage(PackageID packageID) {

        return ReturnOrCalcelPackage(packageID, UserPackageDetail.Status.RETURN);
    }

    public Mono<ResponseEntity<Format>> CancelPackage(PackageID packageID) {

        return ReturnOrCalcelPackage(packageID, UserPackageDetail.Status.CANCEL);
    }

    public Mono<ResponseEntity<Format>> DeletePackage(PackageID packageID) {

        return userPackageDetailRepository.DeleteByID(packageID.getGroup_id(), packageID.getDevice_id(), packageID.getPackage_id())
                .then(Mono.just(ok(Format.builder().response("done").build())));
    }

    public Flux<com.example.heroku.model.UserPackage> DeleteByBeerUnit(PackageItemRemove beerUnitDelete, UserPackageDetail.Status status) {
        return userPackageRepository.DeleteProductByBeerUnit(beerUnitDelete.getGroup_id(), beerUnitDelete.getDevice_id(), beerUnitDelete.getUnit_id(), status);
    }

    public Flux<com.example.heroku.model.UserPackage> DeleteByUserID(UserID userID, UserPackageDetail.Status status) {
        return userPackageDetailRepository.DeleteByUserID(userID.getGroup_id(), userID.getId(), status)
                .thenMany(userPackageRepository.DeleteProductByUserID(userID.getGroup_id(), userID.getId(), status));
    }

    public Flux<com.example.heroku.model.UserPackage> GetByStatusBetween(UserPackageID userPackageID){
        return userPackageRepository.GetByStatusBetween(userPackageID.getGroup_id(), userPackageID.getProduct_second_id(), userPackageID.getProduct_unit_second_id(),
                userPackageID.getFrom(), userPackageID.getTo(), userPackageID.getStatus(), userPackageID.getPage(), userPackageID.getSize());
    }

    public Flux<com.example.heroku.model.UserPackage> GetByStatusOfProduct(UserPackageID userPackageID) {
        long id = userPackageID.getAfter_id();
        if (id <= 0) {
            return userPackageRepository.GetByStatusOfProduct(userPackageID.getGroup_id(), userPackageID.getProduct_second_id(),
                    userPackageID.getStatus(), userPackageID.getPage(), userPackageID.getSize());
        }
        return userPackageRepository.GetByStatusOfProductAfterID(userPackageID.getGroup_id(), userPackageID.getProduct_second_id(),
                id, userPackageID.getStatus(), userPackageID.getPage(), userPackageID.getSize());
    }

    public Flux<PackageDataResponse> GetPackageByDebtOfUser(String groupID, String deviceID) {
        return joinUserPackageDetailWithUserPackgeRepository.getUserPackageDetailByDeviceIDDebt(groupID, deviceID)
                .groupBy(UserPackageDetailJoinWithUserPackage::getPackage_second_id)
                .flatMap(groupedFlux -> groupedFlux.collectList().map(UserPackageDetailJoinWithUserPackage::GeneratePackageData))
                .flatMap(this::fillProductAndBuyer);
    }

    public Flux<PackageDataResponse> GetDonePackageOfStorWithPaymentStatus(String paymentStatus, String metaSearch) {
        return userPackageDetailRepository.GetDonePackageOfStorWithPaymentStatus(paymentStatus, metaSearch)
                .map(PackageDataResponse::new);
    }
}
