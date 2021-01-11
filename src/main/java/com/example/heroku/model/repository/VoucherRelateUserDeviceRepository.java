package com.example.heroku.model.repository;

import com.example.heroku.model.VoucherRelateUserDevice;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VoucherRelateUserDeviceRepository extends ReactiveCrudRepository<VoucherRelateUserDevice, String> {
    @Query(value = "DELETE FROM voucher_relate_user_device WHERE voucher_relate_user_device.voucher_second_id = :id")
    Mono<VoucherRelateUserDevice> deleteByVoucherSecondId(@Param("id")String id);

    @Query(value = "SELECT * FROM voucher_relate_user_device WHERE voucher_relate_user_device.voucher_second_id = :id")
    Flux<VoucherRelateUserDevice> getByVoucherSecondId(@Param("id")String id);

    @Query(value = "DELETE FROM voucher_relate_user_device WHERE voucher_relate_user_device.voucher_second_id = :id AND voucher_relate_user_device.device_id = :device_id")
    Mono<VoucherRelateUserDevice> deleteByVoucherSecondIdAndUserDeviceID(@Param("id")String voucherSecondID, @Param("device_id")String userDeviceID);

    @Query(value = "SELECT * FROM voucher_relate_user_device WHERE voucher_relate_user_device.voucher_second_id = :id AND voucher_relate_user_device.device_id = :device_id")
    Mono<VoucherRelateUserDevice> getByVoucherSecondIdAndUserDeviceID(@Param("id")String voucherSecondID, @Param("device_id")String userDeviceID);
}
