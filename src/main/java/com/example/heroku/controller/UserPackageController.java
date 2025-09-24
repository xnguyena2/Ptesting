package com.example.heroku.controller;

import com.example.heroku.model.UserPackageDetail;
import com.example.heroku.model.Users;
import com.example.heroku.request.beer.PackageItemRemove;
import com.example.heroku.request.beer.ProductPackage;
import com.example.heroku.request.beer.ProductPackgeWithTransaction;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.client.PackageID;
import com.example.heroku.request.client.UserID;
import com.example.heroku.request.client.UserPackageID;
import com.example.heroku.response.Format;
import com.example.heroku.response.PackageDataResponse;
import com.example.heroku.services.UserPackageDetailCounterServices;
import com.example.heroku.services.UserPackage;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;


@RestController
@RequestMapping("/package")
public class UserPackageController {

    @Autowired
    com.example.heroku.services.UserAccount userAccount;

    @Autowired
    UserPackage userPackageAPI;

    @Autowired
    UserPackageDetailCounterServices userPackageDetailCounterServices;

    @PostMapping("/add")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Format>> addBeerToPackage(@RequestBody @Valid ProductPackage productPackage) {
        System.out.println("add beer to package: " + productPackage.getPackage_second_id() + ", group: " + productPackage.getGroup_id());
        return userPackageAPI.AddProductToPackage(productPackage.SetProductPackageForWeb());
    }

    @PostMapping("/update")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Format>> addOrUpdatePackage(@RequestBody @Valid ProductPackage productPackage) {
        System.out.println("save package: " + productPackage.getPackage_second_id() + ", group: " + productPackage.getGroup_id());
        return userPackageAPI.SavePackage(productPackage);
    }

    @PostMapping("/updatenotcheckwithtransacction")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Format>> addOrUpdatePackageWithoutCheckWithTransaction(@RequestBody @Valid ProductPackgeWithTransaction productPackgeWithTransaction) {
        System.out.println("save package without check with transaction : " + productPackgeWithTransaction.getProductPackage().getPackage_second_id() + ", group: " + productPackgeWithTransaction.getProductPackage().getGroup_id());
        return userPackageAPI.SavePackageWithoutCheckWithTransaction(productPackgeWithTransaction);
    }

    @PostMapping("/updatenotcheck")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Format>> addOrUpdatePackageWithoutCheck(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid ProductPackage productPackage) {
        System.out.println("++ save package without check: " + productPackage.getPackage_second_id() + ", group: " + productPackage.getGroup_id());
        return principal.flatMap(users -> userAccount.getUser(users.getUsername()))
                .flatMap(users -> {
                    if (!users.isActive()) {
                        return Mono.just(org.springframework.http.ResponseEntity.badRequest().body(Format.builder().response("user not active!").build()));
                    }
                    return userPackageAPI.SavePackageWithoutCheck(productPackage)
                            .onErrorResume(throwable -> {
                                String msg = throwable.getMessage();
                                System.out.println(msg);
                                return Mono.just(org.springframework.http.ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body(Format.builder().response(msg).build()));
                            });
                });
    }

    @PostMapping("/updateprintkitchen")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Format>> updatePrintKitchen(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid ProductPackage productPackage) {
        System.out.println("update print kitchen: " + productPackage.getPackage_second_id() + ", group: " + productPackage.getGroup_id());

        return userPackageAPI.SavePackageDetailKitchen(productPackage)
                .onErrorResume(throwable -> {
                    String msg = throwable.getMessage();
                    System.out.println(msg);
                    return Mono.just(org.springframework.http.ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Format.builder().response(msg).build()));
                });
    }

    @PostMapping("/admin/delete")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Format>> deletePackage(@RequestBody @Valid PackageID packageID) {
        System.out.println("delete package: " + packageID.getPackage_id() + ", group: " + packageID.getGroup_id());
        return userPackageAPI.DeletePackage(packageID);
    }


    @PostMapping("/remove")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<com.example.heroku.model.UserPackage> removeFromPckage(@RequestBody @Valid PackageItemRemove beerUnitDelete) {
        System.out.println("remove item from package device: " + beerUnitDelete.getDevice_id() + ", group: " + beerUnitDelete.getGroup_id());
        return userPackageAPI.DeleteByBeerUnit(beerUnitDelete, UserPackageDetail.Status.WEB_TEMP);
    }


    @PostMapping("/getbydevice")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<PackageDataResponse> getAll(@RequestBody @Valid UserID userID) {
        System.out.println("Get all my package: " + userID.getId() + ", group: " + userID.getGroup_id());
        return userPackageAPI.GetMyPackage(userID);
    }


    @PostMapping("/getmyworkiung")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<PackageDataResponse> getMyWorking(@RequestBody @Valid UserID userID) {
        System.out.println("Get all my working package: " + userID.getId() + ", group: " + userID.getGroup_id());
        return userPackageAPI.GetMyPackageWorking(userID);
    }


    @PostMapping("/getmypackageprocessing")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<PackageDataResponse> getMyPackage(@RequestBody @Valid UserID userID) {
        System.out.println("Get all my package by status create: " + userID.getId() + ", group: " + userID.getGroup_id());
        if (userID.getAfter_id() > 0) {
            return userPackageAPI.GetMyPackageOfStatusAfterID(userID, UserPackageDetail.Status.CREATE);
        }
        return userPackageAPI.GetMyPackageOfStatus(userID, UserPackageDetail.Status.CREATE);
    }


    @PostMapping("/getmypackagereturn")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<PackageDataResponse> getMyPackageReturn(@RequestBody @Valid UserID userID) {
        System.out.println("Get all my package by status return: " + userID.getId() + ", group: " + userID.getGroup_id());
        if (userID.getAfter_id() > 0) {
            return userPackageAPI.GetMyPackageOfStatusAfterID(userID, UserPackageDetail.Status.RETURN);
        }
        return userPackageAPI.GetMyPackageOfStatus(userID, UserPackageDetail.Status.RETURN);
    }


    @PostMapping("/getbydeviceforweb")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<PackageDataResponse> getAllOfWeb(@RequestBody @Valid UserID packageID) {
        System.out.println("Get all my package for web: " + packageID.getId() + ", group: " + packageID.getGroup_id());
        return userPackageAPI.GetMyPackageOfStatus(UserID.builder().group_id(packageID.getGroup_id()).id(packageID.getId()).page(packageID.getPage()).size(packageID.getSize()).build(), UserPackageDetail.Status.WEB_TEMP);
    }


    @PostMapping("/getbyid")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<PackageDataResponse> getByIDOfDevice(@RequestBody @Valid PackageID packageID) {
        System.out.println("Group: " + packageID.getGroup_id() + ", Get package: " + packageID.getPackage_id() + ", device: " + packageID.getDevice_id());
        return userPackageAPI.GetPackage(packageID);
    }


    @PostMapping("/getbyjustid")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<PackageDataResponse> getJustByID(@RequestBody @Valid PackageID packageID) {
        System.out.println("Group: " + packageID.getGroup_id() + ", Get package: " + packageID.getPackage_id());
        return userPackageAPI.GetJustByPackageId(packageID);
    }


    @PostMapping("/getbygroup")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<PackageDataResponse> getByGroup(@RequestBody @Valid UserID userID) {
        System.out.println("Get all package by group: " + userID.getGroup_id());
        return userPackageAPI.GetPackageByGroup(userID);
    }


    @PostMapping("/getbygrouponlywithbuyer")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<PackageDataResponse> getByGroupOnlyBuyer(@RequestBody @Valid UserID userID) {
        System.out.println("Get all package by group only with buyer data: " + userID.getGroup_id());
        return userPackageAPI.GetPackageByGroupOnlyBuyerData(userID);
    }

    @GetMapping("/countprocessingandweb/{groupid}")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<com.example.heroku.response.CounterUserPackageDetailNumberOfProcessingAndWeb> getProcessingAndWebCounter(@PathVariable("groupid") String groupID) {
        System.out.println("Get count of processing and web package by group: " + groupID);
        return  userPackageDetailCounterServices.count(groupID);
    }


    @PostMapping("/getwokingbygroup")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<PackageDataResponse> getWorkingByGroup(@RequestBody @Valid UserID userID) {
        System.out.println("Get all working package by group: " + userID.getGroup_id());
        return userPackageAPI.GetWorkingPackageByGroupByJoinWith(userID);//.GetWorkingPackageByGroup(userID);
    }


    @PostMapping("/searchwokingbygroup")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<PackageDataResponse> getWorkingByGroup(@RequestBody @Valid SearchQuery searchQuery) {
        System.out.println("Search all working package by group: " + searchQuery.getGroup_id());
        return userPackageAPI.SearchWorkingPackageByGroupByJoinWith(searchQuery);//.GetWorkingPackageByGroup(userID);
    }


    @PostMapping("/getbygroupstatus")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<PackageDataResponse> getByGroupAnsStatus(@RequestBody @Valid PackageID packageID) {
        System.out.println("Get all status package by group: " + packageID.getGroup_id());
        return userPackageAPI.GetPackageByGroupAndStatus(UserID.builder().id(packageID.getPackage_id()).group_id(packageID.getGroup_id()).page(packageID.getPage()).size(packageID.getSize()).build(), packageID.getStatus());
    }


    @PostMapping("/buyerfromwebsubmit")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Format>> buyerFromWebSubmitPackage(@RequestBody @Valid ProductPackage productPackage) {
        System.out.println("buyer from web submit package: " + productPackage.getPackage_second_id() + ", group: " + productPackage.getGroup_id());
        return userPackageAPI.BuyerFromWebSubmitPackage(productPackage);
    }


    @PostMapping("/return")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Format>> returnPackage(@RequestBody @Valid PackageID packageID) {
        System.out.println("-- return package: " + packageID.getPackage_id() + ", group: " + packageID.getGroup_id());
        return userPackageAPI.ReturnPackage(packageID);
    }


    @PostMapping("/cancel")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Format>> cancelPackage(@RequestBody @Valid PackageID packageID) {
        System.out.println("cancel package: " + packageID.getPackage_id() + ", group: " + packageID.getGroup_id());
        return userPackageAPI.CancelPackage(packageID);
    }

    @PostMapping("/clean")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<com.example.heroku.model.UserPackage> clean(@RequestBody @Valid UserID userID) {
        System.out.println("clean all package with device_id: " + userID.getId() + ", group: " + userID.getGroup_id());
        return userPackageAPI.DeleteByUserID(userID, UserPackageDetail.Status.WEB_TEMP);
    }

    @PostMapping("/getbystatusbetween")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<com.example.heroku.model.UserPackage> getStatictisByStatusBetween(@RequestBody @Valid UserPackageID userPackageID) {
        System.out.println("get statictis of group: " + userPackageID.getGroup_id() + ", product: " + userPackageID.getProduct_second_id() + ", unit: " + userPackageID.getProduct_unit_second_id() + ", from: " + userPackageID.getFrom() + ", to: " + userPackageID.getTo());
        return userPackageAPI.GetByStatusBetween(userPackageID);
    }

    @PostMapping("/getbystatusproduct")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<com.example.heroku.model.UserPackage> getStatictisByStatusOfProduct(@RequestBody @Valid UserPackageID userPackageID) {
        System.out.println("get statictis only for product of group: " + userPackageID.getGroup_id() + ", product: " + userPackageID.getProduct_second_id() + ", after id: " + userPackageID.getAfter_id());
        return userPackageAPI.GetByStatusOfProduct(userPackageID);
    }
}
