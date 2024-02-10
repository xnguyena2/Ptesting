package com.example.heroku.services;

import com.example.heroku.model.UserPackageDetail;
import com.example.heroku.model.repository.UserPackageDetailRepository;
import com.example.heroku.model.repository.UserPackageRepository;
import com.example.heroku.request.beer.BeerSubmitData;
import com.example.heroku.request.beer.ProductPackage;
import com.example.heroku.request.beer.PackageItemRemove;
import com.example.heroku.request.beer.ProductPackgeWithTransaction;
import com.example.heroku.request.carousel.IDContainer;
import com.example.heroku.request.client.PackageID;
import com.example.heroku.request.client.UserID;
import com.example.heroku.response.Format;
import com.example.heroku.response.PackageDataResponse;
import com.example.heroku.response.ProductInPackageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    PaymentTransation paymentTransation;

    public Mono<ResponseEntity<Format>> AddProductToPackage(ProductPackage productPackage) {

        if (productPackage.isEmpty())
            return Mono.just(badRequest().body(Format.builder().response("user package empty").build()));
        return
                Mono.just(productPackage)
                        .flatMap(this::saveBuyer)
                        .flatMap(this::savePackageDetail)
                        .flatMap(productPackage1 -> Flux.just(productPackage1.getUserPackage())
                                .flatMap(userPackage ->
                                        userPackageRepository.AddPackage(userPackage.getGroup_id(), userPackage.getDevice_id(), userPackage.getPackage_second_id(), userPackage.getProduct_second_id(), userPackage.getProduct_unit_second_id(),
                                                userPackage.getNumber_unit(), userPackage.getStatus())

                                )
                                .then(Mono.just(ok(Format.builder().response("done").build())))
                        );
    }

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
                        .filter(productPackage1 -> productPackage1.getUserPackage() != null)
                        .flatMapMany(productPackage1 -> Flux.just(productPackage1.getUserPackage()))
                        .flatMap(this::savePackageItem)
                        .then(Mono.just(ok(Format.builder().response("done").build())));
    }

    public Mono<ResponseEntity<Format>> SavePackageWithoutCheckWithTransaction(ProductPackgeWithTransaction productPackgeWithTransaction) {
        return paymentTransation.insertOrUpdate(productPackgeWithTransaction.getTransation())
                .then(SavePackageWithoutCheck(productPackgeWithTransaction.getProductPackage()));
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
                        .filter(productPackage1 -> productPackage1.getUserPackage() != null)
                        .flatMapMany(productPackage1 -> Flux.just(productPackage1.getUserPackage()))
                        .flatMap(this::savePackageItemWithoutCheck)
                        .then(Mono.just(ok(Format.builder().response("done").build())));
    }

    Mono<ProductPackage> savePackageDetail(ProductPackage productPackage) {
        UserPackageDetail detail = productPackage;
        if (productPackage.getPackage_second_id() == null) {
            detail = null;
        }
        productPackage.AutoFill();
        if (detail == null) {
            detail = productPackage.getUserPackageDetail();
        }
        return savePackageDetail(detail)
                .then(Mono.just(productPackage));
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
        float discount = productPackage.getStatus() == UserPackageDetail.Status.DONE ? productPackage.getDiscount_amount() + productPackage.getDiscount_percent() * productPackage.getPrice() : 0;
        float ship = productPackage.getStatus() == UserPackageDetail.Status.DONE ? productPackage.getShip_price() : 0;
        int point = productPackage.getStatus() == UserPackageDetail.Status.DONE ? productPackage.getPoint() : 0;
        return buyer.insertOrUpdate(buyerInfo, totalPrice, realPrice, ship, discount, point).then(Mono.just(productPackage));
    }

    Mono<UserPackageDetail> removePackgeOfBuyer(UserPackageDetail productPackage) {

        String device_id = productPackage.getDevice_id();
        if (device_id != null && productPackage.getStatus() == UserPackageDetail.Status.DONE) {
            float totalPrice = -productPackage.getPrice();
            float realPrice = -productPackage.getPayment();
            float discount = -(productPackage.getDiscount_amount() + productPackage.getDiscount_percent() * productPackage.getPrice());
            float ship = -productPackage.getShip_price();
            int point = -productPackage.getPoint();
            return buyer.updatePrice(productPackage.getGroup_id(), device_id, totalPrice, realPrice, ship, discount, point).then(Mono.just(productPackage));
        }
        return Mono.just(productPackage);
    }

    Mono<UserPackageDetail> savePackageDetail(UserPackageDetail detail) {
        return userPackageDetailRepository.InsertOrUpdate(detail.getGroup_id(), detail.getDevice_id(), detail.getStaff_id(), detail.getPackage_second_id(),
                detail.getPackage_type(), detail.getVoucher(),
                detail.getArea_id(), detail.getArea_name(), detail.getTable_id(), detail.getTable_name(),
                detail.getPrice(), detail.getPayment(), detail.getDiscount_amount(), detail.getDiscount_percent(), detail.getShip_price(), detail.getCost(), detail.getProfit(),
                detail.getPoint(), detail.getNote(), detail.getImage(), detail.getProgress(), detail.getStatus(), detail.getCreateat());
    }

    Mono<com.example.heroku.model.UserPackage> savePackageItem(com.example.heroku.model.UserPackage userPackage) {
        return userPackageRepository.InsertOrUpdatePackage(userPackage.getGroup_id(), userPackage.getDevice_id(), userPackage.getPackage_second_id(), userPackage.getProduct_second_id(), userPackage.getProduct_unit_second_id(),
                userPackage.getNumber_unit(), userPackage.getBuy_price(), userPackage.getPrice(), userPackage.getDiscount_amount(), userPackage.getDiscount_percent(),
                userPackage.getNote(), userPackage.getStatus(), userPackage.getCreateat());
    }

    Mono<com.example.heroku.model.UserPackage> savePackageItemWithoutCheck(com.example.heroku.model.UserPackage userPackage) {
        return userPackageRepository.InsertOrUpdatePackageWithoutCheck(userPackage.getGroup_id(), userPackage.getDevice_id(), userPackage.getPackage_second_id(), userPackage.getProduct_second_id(), userPackage.getProduct_unit_second_id(),
                userPackage.getNumber_unit(), userPackage.getBuy_price(), userPackage.getPrice(), userPackage.getDiscount_amount(), userPackage.getDiscount_percent(),
                userPackage.getNote(), userPackage.getStatus(), userPackage.getCreateat());
    }

    Mono<PackageDataResponse> fillPackageItem(PackageDataResponse packageDataResponse) {
        return
                userPackageRepository.GetDevicePackageWithID(packageDataResponse.getGroup_id(), packageDataResponse.getPackage_second_id())
                        .flatMap(userPackage ->
                                beerAPI.GetBeerByIDWithUnit(userPackage.getGroup_id(), userPackage.getProduct_second_id(), userPackage.getProduct_unit_second_id())
                                        .switchIfEmpty(Mono.just(BeerSubmitData.builder().build()))
                                        .map(beerSubmitData -> new ProductInPackageResponse(userPackage).SetBeerData(beerSubmitData))
                        )
                        .map(packageDataResponse::addItem)
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


    public Flux<PackageDataResponse> GetMyPackageOfStatus(UserID userID, UserPackageDetail.Status status) {
        return userPackageDetailRepository.GetDevicePackageDetailByStatus(userID.getGroup_id(), userID.getId(), status, userID.getPage(), userID.getSize())
                .map(PackageDataResponse::new)
                .flatMap(this::fillPackageItem);
    }

    public Flux<PackageDataResponse> GetPackageByGroup(UserID userID) {
        return userPackageDetailRepository.GetAllPackageDetail(userID.getGroup_id(), userID.getPage(), userID.getSize())
                .map(PackageDataResponse::new)
                .flatMap(this::fillPackageItem);
    }

    public Flux<PackageDataResponse> GetWorkingPackageByGroup(UserID userID) {
        return GetPackageByGrouAndStatus(userID, UserPackageDetail.Status.CREATE, UserPackageDetail.Status.DONE);
    }

    public Flux<PackageDataResponse> GetPackageByGrouAndStatus(UserID userID, UserPackageDetail.Status status) {
        return userPackageDetailRepository.GetAllPackageDetailByStatus(userID.getGroup_id(), status, userID.getPage(), userID.getSize())
                .map(PackageDataResponse::new)
                .flatMap(this::fillPackageItem);
    }

    public Flux<PackageDataResponse> GetPackageByGrouAndStatus(UserID userID, UserPackageDetail.Status status, UserPackageDetail.Status or_status) {
        return userPackageDetailRepository.GetAllPackageDetailByStatus(userID.getGroup_id(), status, or_status, userID.getPage(), userID.getSize())
                .map(PackageDataResponse::new)
                .flatMap(this::fillPackageItem);
    }

    public Mono<PackageDataResponse> GetPackage(PackageID packageID) {

        return userPackageDetailRepository.GetPackageDetail(packageID.getGroup_id(), packageID.getDevice_id(), packageID.getPackage_id())
                .map(PackageDataResponse::new)
                .flatMap(this::fillPackageItem);
    }

    public Mono<PackageDataResponse> GetJustByPackageId(PackageID packageID) {

        return userPackageDetailRepository.GetPackageDetailById(packageID.getGroup_id(), packageID.getPackage_id())
                .map(PackageDataResponse::new)
                .flatMap(this::fillPackageItem);
    }

    private Mono<ResponseEntity<Format>> ReturnOrCalcelPackage(PackageID packageID, UserPackageDetail.Status status) {

        return userPackageDetailRepository.GetPackageDetailByID(packageID.getGroup_id(), packageID.getPackage_id())
                .filter(sd -> sd.getStatus() == UserPackageDetail.Status.CREATE && status == UserPackageDetail.Status.CANCEL ||
                        sd.getStatus() == UserPackageDetail.Status.DONE && status == UserPackageDetail.Status.RETURN)
                .flatMap(userPackageDetail -> paymentTransation.deleteOfPackgeID(IDContainer.builder().group_id(packageID.getGroup_id()).id(packageID.getPackage_id()).build())
                        .thenMany(userPackageRepository.UpdateStatusByPackgeID(packageID.getGroup_id(), packageID.getPackage_id(), status))
                        .then(
                                userPackageDetailRepository.UpdateStatusByID(packageID.getGroup_id(), packageID.getPackage_id(), status)
                        )
                        .thenMany(removePackgeOfBuyer(userPackageDetail))
                        .then(Mono.just(ok(Format.builder().response("done").build())))
                );
    }

    public Mono<ResponseEntity<Format>> ReturnPackage(PackageID packageID) {

        return ReturnOrCalcelPackage(packageID, UserPackageDetail.Status.RETURN);
    }

    public Mono<ResponseEntity<Format>> CancelPackage(PackageID packageID) {

        return ReturnOrCalcelPackage(packageID, UserPackageDetail.Status.CANCEL);
    }

    public Mono<ResponseEntity<Format>> DeletePackage(PackageID packageID) {

        return paymentTransation.deleteOfPackgeID(IDContainer.builder().group_id(packageID.getGroup_id()).id(packageID.getPackage_id()).build())
                .then(
                        userPackageDetailRepository.DeleteByID(packageID.getGroup_id(), packageID.getDevice_id(), packageID.getPackage_id())
                )
                .then(Mono.just(ok(Format.builder().response("done").build())));
    }

    public Flux<com.example.heroku.model.UserPackage> DeleteByBeerUnit(PackageItemRemove beerUnitDelete) {
        return userPackageRepository.DeleteProductByBeerUnit(beerUnitDelete.getGroup_id(), beerUnitDelete.getDevice_id(), beerUnitDelete.getUnit_id());
    }

    public Flux<com.example.heroku.model.UserPackage> DeleteByUserID(UserID userID) {
        return userPackageDetailRepository.DeleteByUserID(userID.getGroup_id(), userID.getId())
                .thenMany(userPackageRepository.DeleteProductByUserID(userID.getGroup_id(), userID.getId()));
    }
}
