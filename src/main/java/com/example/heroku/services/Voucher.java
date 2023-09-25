package com.example.heroku.services;

import com.example.heroku.model.VoucherRelateProduct;
import com.example.heroku.model.VoucherRelateUserDevice;
import com.example.heroku.model.repository.VoucherRelateBeerRepository;
import com.example.heroku.model.repository.VoucherRelateUserDeviceRepository;
import com.example.heroku.model.repository.VoucherRepository;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.voucher.VoucherData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

import static org.springframework.http.ResponseEntity.ok;

@Component
public class Voucher {

    @Autowired
    VoucherRepository voucherRepository;

    @Autowired
    VoucherRelateBeerRepository voucherRelateBeerRepository;

    @Autowired
    VoucherRelateUserDeviceRepository voucherRelateUserDeviceRepository;

    public Mono<com.example.heroku.model.Voucher> deleteByID(VoucherData voucherData) {
        String groupID = voucherData.getGroup_id();
        String voucherID = voucherData.getVoucher_second_id();
        return voucherRelateUserDeviceRepository.deleteByVoucherSecondId(groupID, voucherID)
                .then(voucherRelateBeerRepository.deleteByVoucherSecondId(groupID, voucherID))
                .then(voucherRepository.deleteBySecondId(groupID, voucherID));
    }

    public Mono<com.example.heroku.model.Voucher> createVoucher(VoucherData voucherData) {
        com.example.heroku.model.Voucher voucher = voucherData.GetVoucher();
        return voucherRepository.deleteBySecondId(voucher.getGroup_id(), voucher.getVoucher_second_id())
                .then(voucherRepository.save(voucher.AutoFill()))
                .then(voucherRelateBeerRepository.deleteByVoucherSecondId(voucher.getGroup_id(), voucher.getVoucher_second_id()))
                .then(voucherRelateUserDeviceRepository.deleteByVoucherSecondId(voucher.getGroup_id(), voucher.getVoucher_second_id()))
                .thenMany(Mono.just(voucherData)
                        .filter(vd -> !vd.isFor_all_user())
                        .flatMap(vd ->
                                voucherRelateUserDeviceRepository.saveAll(Flux.just(vd.getListUser())
                                                .distinct()
                                                .map(
                                                        userID ->
                                                                VoucherRelateUserDevice.builder()
                                                                        .group_id(vd.getGroup_id())
                                                                        .voucher_second_id(vd.getVoucher_second_id())
                                                                        .device_id(userID)
                                                                        .reuse(vd.getReuse())
                                                                        .build()
                                                                        .AutoFill()
                                                )
                                        )
                                        .then()
                        )
                )
                .then(Mono.just(voucherData)
                        .filter(vd -> !vd.isFor_all_product() && !vd.isPackage_voucher())
                        .flatMap(vd ->
                                voucherRelateBeerRepository.saveAll(Flux.just(vd.getListBeer())
                                                .distinct()
                                                .map(
                                                        beerID ->
                                                                VoucherRelateProduct
                                                                        .builder()
                                                                        .group_id(voucher.getGroup_id())
                                                                        .product_second_id(beerID)
                                                                        .voucher_second_id(voucher.getVoucher_second_id())
                                                                        .build()
                                                                        .AutoFill()
                                                )
                                        )
                                        .then()
                        )
                )
                .then(Mono.just(voucher));
    }

    private Mono<VoucherData> ConvertVoucherToVoucherData(Mono<com.example.heroku.model.Voucher> v) {
        return v
                .map(voucher -> VoucherData.builder().build().FromVoucher(voucher))
                .flatMap(voucherData ->
                        Mono.just(new ArrayList<String>())
                                .flatMap(listProduct ->
                                        voucherRelateBeerRepository.getByVoucherSecondId(voucherData.getGroup_id(), voucherData.getVoucher_second_id())
                                                .map(voucherRelateProduct -> listProduct.add(voucherRelateProduct.getProduct_second_id()))
                                                .then(Mono.just(listProduct))
                                                .map(voucherData::SetListProduct)
                                ).then(Mono.just(voucherData))
                )
                .flatMap(voucherData ->
                        Mono.just(new ArrayList<String>())
                                .flatMap(listDevice ->
                                        voucherRelateUserDeviceRepository.getByVoucherSecondId(voucherData.getGroup_id(), voucherData.getVoucher_second_id())
                                                .map(voucherRelateBeer -> listDevice.add(voucherRelateBeer.getDevice_id()))
                                                .then(Mono.just(listDevice))
                                                .map(voucherData::SetListUser)
                                ).then(Mono.just(voucherData))
                );
    }

    public Flux<VoucherData> getAllVoucher(SearchQuery query) {
        String groupID = query.getGroup_id();
        int page = query.getPage();
        int size = query.getSize();
        return voucherRepository.findAll(groupID, page, size)
                .flatMap(voucher -> this.ConvertVoucherToVoucherData(Mono.just(voucher)));
    }

    public Mono<VoucherData> getVoucherByID(String groupID, String voucherID) {
        return voucherRepository.getVoucherBySecondID(groupID, voucherID)
                .flatMap(voucher -> this.ConvertVoucherToVoucherData(Mono.just(voucher)));
    }

    public Mono<com.example.heroku.model.Voucher> getDeviceVoucher(String groupID, String voucherID, String device, String productID) {
        return voucherRepository.getVoucherBySecondIDAndUserDeviceAndBeerSecondID(groupID, voucherID, device, productID);
    }

    public Mono<com.example.heroku.model.Voucher> getPackageVoucher(String groupID, String voucherID, String device) {
        return voucherRepository.getPackageVoucherBySecondIDAndUserDevice(groupID, voucherID, device);
    }

    public Mono<com.example.heroku.model.Voucher> getVoucher(String groupID, String bySecondID) {
        return voucherRepository.getVoucherBySecondID(groupID, bySecondID);
    }

    public Mono<com.example.heroku.model.Voucher> getDiscount(String groupID, String voucherSecondID, String userDeviceID, String beerSecondID) {
        return voucherRepository.getVoucherBySecondIDAndUserDeviceAndBeerSecondID(groupID, voucherSecondID, userDeviceID, beerSecondID);
    }

    public Mono<com.example.heroku.model.Voucher> comsumeVoucher(String groupID, String voucherSecondID, String userDeviceID, String beerSecondID) {
        return voucherRepository.getVoucherBySecondIDAndUserDeviceAndBeerSecondID(groupID, voucherSecondID, userDeviceID, beerSecondID)
                .flatMap(voucher ->
                        voucherRelateUserDeviceRepository.getByVoucherSecondIdAndUserDeviceID(groupID, voucherSecondID, userDeviceID)
                                .switchIfEmpty(
                                        Mono.just(VoucherRelateUserDevice.builder()
                                                .group_id(groupID)
                                                .reuse(voucher.getReuse())
                                                .device_id(userDeviceID)
                                                .voucher_second_id(voucherSecondID)
                                                .build()
                                        )
                                )
                                .map(
                                        voucherRelateUserDevice -> voucherRelateUserDevice.ResetID().ComsumeVoucher().AutoFill()
                                )
                                .flatMap(voucherRelateUserDevice ->
                                        voucherRelateUserDeviceRepository.updateOrInsert(voucherRelateUserDevice.getGroup_id(), voucherRelateUserDevice.getVoucher_second_id(), voucherRelateUserDevice.getDevice_id(), voucherRelateUserDevice.getReuse())
                                                .then(Mono.just(voucher))
                                )
                );
    }

    /*
    only call this function if the voucher alredy consume
     */
    public Mono<VoucherRelateUserDevice> ForceSaveVoucher(String groupID, String voucherSecondID, String userDeviceID, int resue) {
        System.out.println("Save voucher: " + voucherSecondID + ", reuse: " + resue + ", group: " + groupID);
        return voucherRelateUserDeviceRepository.updateOrInsert(groupID, voucherSecondID, userDeviceID, resue);
    }

    public Flux<com.example.heroku.model.Voucher> getAllMyVoucher(String groupID, String userDeviceID) {
        return voucherRepository.getAllVoucherOfUserDevice(groupID, userDeviceID);
    }
}
