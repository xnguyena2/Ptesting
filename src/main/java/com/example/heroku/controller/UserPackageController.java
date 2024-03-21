package com.example.heroku.controller;

import com.example.heroku.model.UserPackageDetail;
import com.example.heroku.request.beer.ProductPackage;
import com.example.heroku.request.beer.PackageItemRemove;
import com.example.heroku.request.beer.ProductPackgeWithTransaction;
import com.example.heroku.request.client.PackageID;
import com.example.heroku.request.client.UserID;
import com.example.heroku.response.Format;
import com.example.heroku.response.PackageDataResponse;
import com.example.heroku.response.ProductInPackageResponse;
import com.example.heroku.services.UserPackage;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/package")
public class UserPackageController {

    @Autowired
    UserPackage userPackageAPI;

    @PostMapping("/add")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Format>> addBeerToPackage(@RequestBody @Valid ProductPackage productPackage) {
        System.out.println("add beer to package: " + productPackage.getPackage_second_id());
        return userPackageAPI.AddProductToPackage(productPackage.SetProductPackageForWeb());
    }

    @PostMapping("/update")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Format>> addOrUpdatePackage(@RequestBody @Valid ProductPackage productPackage) {
        System.out.println("save package: " + productPackage.getPackage_second_id());
        return userPackageAPI.SavePackage(productPackage);
    }

    @PostMapping("/updatenotcheckwithtransacction")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Format>> addOrUpdatePackageWithoutCheckWithTransaction(@RequestBody @Valid ProductPackgeWithTransaction productPackgeWithTransaction) {
        System.out.println("save package without check with transaction : " + productPackgeWithTransaction.getProductPackage().getPackage_second_id());
        return userPackageAPI.SavePackageWithoutCheckWithTransaction(productPackgeWithTransaction);
    }

    @PostMapping("/updatenotcheck")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Format>> addOrUpdatePackageWithoutCheck(@RequestBody @Valid ProductPackage productPackage) {
        System.out.println("save package without check: " + productPackage.getPackage_second_id());
        return userPackageAPI.SavePackageWithoutCheck(productPackage);
    }

    @PostMapping("/admin/delete")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Format>> deletePackage(@RequestBody @Valid PackageID packageID) {
        System.out.println("delete package: " + packageID.getPackage_id());
        return userPackageAPI.DeletePackage(packageID);
    }


    @PostMapping("/remove")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<com.example.heroku.model.UserPackage> removeFromPckage(@RequestBody @Valid PackageItemRemove beerUnitDelete) {
        return userPackageAPI.DeleteByBeerUnit(beerUnitDelete, UserPackageDetail.Status.WEB_TEMP);
    }


    @PostMapping("/getbydevice")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<PackageDataResponse> getAll(@RequestBody @Valid UserID userID) {
        System.out.println("Get all my package: " + userID.getId());
        return userPackageAPI.GetMyPackage(userID);
    }


    @PostMapping("/getbydeviceforweb")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<PackageDataResponse> getAll(@RequestBody @Valid PackageID packageID) {
        System.out.println("Get all my package for web: " + packageID.getDevice_id());
        return userPackageAPI.GetMyPackageOfStatus(UserID.builder().id(packageID.getDevice_id()).page(packageID.getPage()).size(packageID.getSize()).build(), UserPackageDetail.Status.WEB_TEMP);
    }


    @PostMapping("/getbyid")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<PackageDataResponse> getByID(@RequestBody @Valid PackageID packageID) {
        System.out.println("Get package: " + packageID.getPackage_id());
        return userPackageAPI.GetPackage(packageID);
    }


    @PostMapping("/getbyjustid")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<PackageDataResponse> getJustByID(@RequestBody @Valid PackageID packageID) {
        System.out.println("Get package: " + packageID.getPackage_id());
        return userPackageAPI.GetJustByPackageId(packageID);
    }


    @PostMapping("/getbygroup")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<PackageDataResponse> getByGroup(@RequestBody @Valid UserID userID) {
        System.out.println("Get all package by group: " + userID.getGroup_id());
        return userPackageAPI.GetPackageByGroup(userID);
    }


    @PostMapping("/getwokingbygroup")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<PackageDataResponse> getWorkingByGroup(@RequestBody @Valid UserID userID) {
        System.out.println("Get all working package by group: " + userID.getGroup_id());
        return userPackageAPI.GetWorkingPackageByGroupByJoinWith(userID);//.GetWorkingPackageByGroup(userID);
    }


    @PostMapping("/getbygroupstatus")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<PackageDataResponse> getByGroupAnsStatus(@RequestBody @Valid PackageID packageID) {
        System.out.println("Get all status package by group: " + packageID.getGroup_id());
        return userPackageAPI.GetPackageByGroupAndStatus(UserID.builder().id(packageID.getPackage_id()).group_id(packageID.getGroup_id()).page(packageID.getPage()).size(packageID.getSize()).build(), packageID.getStatus());
    }


    @PostMapping("/buyerfromwebsubmit")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Format>> buyerFromWebSubmitPackage(@RequestBody @Valid PackageID packageID) {
        System.out.println("buyer from web submit package: " + packageID.getPackage_id());
        return userPackageAPI.BuyerFromWebSubmitPackage(packageID);
    }


    @PostMapping("/return")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Format>> returnPackage(@RequestBody @Valid PackageID packageID) {
        System.out.println("return package: " + packageID.getPackage_id());
        return userPackageAPI.ReturnPackage(packageID);
    }


    @PostMapping("/cancel")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Format>> cancelPackage(@RequestBody @Valid PackageID packageID) {
        System.out.println("cancel package: " + packageID.getPackage_id());
        return userPackageAPI.CancelPackage(packageID);
    }

    @PostMapping("/clean")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<com.example.heroku.model.UserPackage> clean(@RequestBody @Valid UserID userID) {
        System.out.println("clean all package with device_id: " + userID.getId());
        return userPackageAPI.DeleteByUserID(userID, UserPackageDetail.Status.WEB_TEMP);
    }
}
