package com.example.heroku.services;

import com.example.heroku.model.VoucherRelateBeer;
import com.example.heroku.model.VoucherRelateUserDevice;
import com.example.heroku.model.repository.VoucherRelateBeerRepository;
import com.example.heroku.model.repository.VoucherRelateUserDeviceRepository;
import com.example.heroku.model.repository.VoucherRepository;
import com.example.heroku.request.voucher.VoucherData;
import com.example.heroku.response.Format;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.ResponseEntity.ok;

@Component
public class Voucher {

    @Autowired
    VoucherRepository voucherRepository;

    @Autowired
    VoucherRelateBeerRepository voucherRelateBeerRepository;

    @Autowired
    VoucherRelateUserDeviceRepository voucherRelateUserDeviceRepository;

    public Mono<ResponseEntity<Format>> generateID() {
        return Mono.just(ok(Format.builder().response(Util.getInstance().GenerateID()).build()));
    }

    public Mono<com.example.heroku.model.Voucher> createVoucher(VoucherData voucherData) {
        return voucherRepository.deleteBySecondId(voucherData.getVoucher().getVoucher_second_id())
                .then(voucherRelateBeerRepository.deleteByVoucherSecondId(voucherData.getVoucher().getVoucher_second_id()))
                .then(voucherRelateUserDeviceRepository.deleteByVoucherSecondId(voucherData.getVoucher().getVoucher_second_id()))
                .thenMany(Mono.just(voucherData)
                        .filter(vd -> !vd.getVoucher().isFor_all_user())
                        .flatMap(vd ->
                                voucherRelateUserDeviceRepository.saveAll(Flux.just(vd.getListUser())
                                        .distinct()
                                        .map(
                                                userID ->
                                                        VoucherRelateUserDevice.builder()
                                                                .voucher_second_id(voucherData.getVoucher().getVoucher_second_id())
                                                                .device_id(userID)
                                                                .reuse(voucherData.getVoucher().getReuse())
                                                                .build()
                                                                .AutoFill()
                                        )
                                )
                                        .then()
                        )
                )
                .then(Mono.just(voucherData)
                        .filter(vd -> !vd.getVoucher().isFor_all_beer())
                        .flatMap(vd ->
                                voucherRelateBeerRepository.saveAll(Flux.just(vd.getListBeer())
                                        .distinct()
                                        .map(
                                                beerID ->
                                                        VoucherRelateBeer
                                                                .builder()
                                                                .beer_second_id(beerID)
                                                                .voucher_second_id(voucherData.getVoucher().getVoucher_second_id())
                                                                .build()
                                                                .AutoFill()
                                        )
                                )
                                        .then()
                        )
                        .then(Mono.just(voucherData.getVoucher())
                                .flatMap(voucher -> voucherRepository.save(voucher.AutoFill()))
                        )
                );
    }

    public Flux<com.example.heroku.model.Voucher> getAllVoucher(int page, int size) {
        return voucherRepository.findAll(page, size);
    }

    public Mono<com.example.heroku.model.Voucher> getDeviceVoucher(String voucherID, String device, String productID) {
        return voucherRepository.getVoucherOfUser(voucherID, productID, device);
    }

    public Mono<com.example.heroku.model.Voucher> getVoucher(String bySecondID) {
        return voucherRepository.getVoucherBySecondID(bySecondID);
    }

    public Mono<com.example.heroku.model.Voucher> getDiscount(String voucherSecondID, String userDeviceID, String beerSecondID) {
        return voucherRepository.getVoucherBySecondIDAndUserDeviceAndBeerSecondID(voucherSecondID, userDeviceID, beerSecondID);
    }

    public Mono<com.example.heroku.model.Voucher> comsumeVoucher(String voucherSecondID, String userDeviceID, String beerSecondID) {
        return voucherRepository.getVoucherBySecondIDAndUserDeviceAndBeerSecondID(voucherSecondID, userDeviceID, beerSecondID)
                .flatMap(voucher ->
                        voucherRelateUserDeviceRepository.getByVoucherSecondIdAndUserDeviceID(voucherSecondID, userDeviceID)
                                .switchIfEmpty(
                                        Mono.just(VoucherRelateUserDevice.builder()
                                                .reuse(voucher.getReuse())
                                                .device_id(userDeviceID)
                                                .voucher_second_id(voucherSecondID)
                                                .build()
                                ))
                                .flatMap(
                                        voucherRelateUserDevice ->
                                                voucherRelateUserDeviceRepository.deleteByVoucherSecondIdAndUserDeviceID(voucherSecondID, userDeviceID)
                                                        .then(voucherRelateUserDeviceRepository.save(voucherRelateUserDevice.ResetID().ComsumeVoucher().AutoFill()))
                                )
                                .then(Mono.just(voucher))
                );
    }

    /*
    only call this function if the voucher alredy consume
     */
    public Mono<VoucherRelateUserDevice> ForceSaveVoucher(String voucherSecondID, String userDeviceID, int resue) {
        System.out.println("Save voucher: " + voucherSecondID + ", reuse: " + resue);
        return voucherRelateUserDeviceRepository.updateOrInsert(voucherSecondID, userDeviceID, resue);
    }

    public Flux<com.example.heroku.model.Voucher> getAllMyVoucher(String userDeviceID){
        return voucherRepository.getAllVoucherOfUserDevice(userDeviceID);
    }
}
