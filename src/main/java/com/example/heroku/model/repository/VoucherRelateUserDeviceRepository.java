package com.example.heroku.model.repository;

import com.example.heroku.model.VoucherRelateUserDevice;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VoucherRelateUserDeviceRepository extends ReactiveCrudRepository<VoucherRelateUserDevice, Long> {
    @Query(value = "DELETE FROM voucher_relate_user_device WHERE voucher_relate_user_device.group_id = :group_id AND voucher_relate_user_device.voucher_second_id = :id")
    Mono<VoucherRelateUserDevice> deleteByVoucherSecondId(@Param("group_id")String group_id, @Param("id") String id);

    @Query(value = "SELECT * FROM voucher_relate_user_device WHERE voucher_relate_user_device.group_id = :group_id AND voucher_relate_user_device.voucher_second_id = :id")
    Flux<VoucherRelateUserDevice> getByVoucherSecondId(@Param("group_id")String group_id, @Param("id") String id);

    @Query(value = "DELETE FROM voucher_relate_user_device WHERE voucher_relate_user_device.group_id = :group_id AND voucher_relate_user_device.voucher_second_id = :id AND voucher_relate_user_device.device_id = :device_id")
    Mono<VoucherRelateUserDevice> deleteByVoucherSecondIdAndUserDeviceID(@Param("group_id")String group_id, @Param("id") String voucherSecondID, @Param("device_id") String userDeviceID);

    @Query(value = "SELECT * FROM voucher_relate_user_device WHERE voucher_relate_user_device.group_id = :group_id AND voucher_relate_user_device.voucher_second_id = :id AND voucher_relate_user_device.device_id = :device_id")
    Mono<VoucherRelateUserDevice> getByVoucherSecondIdAndUserDeviceID(@Param("group_id")String group_id, @Param("id") String voucherSecondID, @Param("device_id") String userDeviceID);

    @Query(value = "INSERT INTO voucher_relate_user_device(group_id, voucher_second_id, REUSE, device_id, createat) VALUES(:group_id, :voucher_second_id, :reuse, :device_id, NOW()) ON CONFLICT ON CONSTRAINT UQ_voucher_relate_user_device DO UPDATE SET REUSE=:reuse, createat=NOW() WHERE voucher_relate_user_device.REUSE>:reuse AND voucher_relate_user_device.voucher_second_id = :voucher_second_id AND voucher_relate_user_device.device_id = :device_id")
    Mono<VoucherRelateUserDevice> updateOrInsert(@Param("group_id")String group_id, @Param("voucher_second_id") String voucherSecondID, @Param("device_id") String userDeviceID, @Param("reuse") int reuse);
}
