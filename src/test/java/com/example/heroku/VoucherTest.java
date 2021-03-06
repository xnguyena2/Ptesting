package com.example.heroku;

import com.example.heroku.request.voucher.VoucherData;
import com.example.heroku.services.Voucher;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class VoucherTest {

    @Autowired
    Voucher voucherAPI;

    public void VoucherTest() {

        voucherAPI.createVoucher(
                VoucherData.builder()
                        .voucher(
                                com.example.heroku.model.Voucher
                                        .builder()
                                        .for_all_beer(true)
                                        .for_all_user(true)
                                        .voucher_second_id("GIAM_5K")
                                        .detail("Giảm 5k trên toàn bộ sản phẩm")
                                        .amount(5000)
                                        .reuse(5)
                                        .build()
                        )
                        .build())
                .block();

        voucherAPI.createVoucher(
                VoucherData.builder()
                        .voucher(
                                com.example.heroku.model.Voucher
                                        .builder()
                                        .for_all_beer(true)
                                        .voucher_second_id("GIAM_30%")
                                        .discount(30)
                                        .reuse(1)
                                        .detail("Giảm 30% trên toàn bộ sản phẩm, chỉ áp dụng cho ai nhận được.")
                                        .build()
                        )
                        .listUser(new String[]{"222222"})
                        .build())
                .block();

        voucherAPI.createVoucher(
                VoucherData.builder()
                        .voucher(
                                com.example.heroku.model.Voucher
                                        .builder()
                                        .voucher_second_id("GIAM_50k")
                                        .detail("Giảm 50k trên 1 loại sản phẩm. Chỉ áp dụng cho ai nhận được.")
                                        .amount(50000)
                                        .reuse(2)
                                        .build()
                        )
                        .listBeer(new String[]{"123","123","123"})
                        .listUser(new String[]{"222222","222222","222222", "333333"})
                        .build())
                .block();

        voucherAPI.createVoucher(
                VoucherData.builder()
                        .voucher(
                                com.example.heroku.model.Voucher
                                        .builder()
                                        .for_all_user(true)
                                        .voucher_second_id("GIAM_BIA_666")
                                        .discount(10)
                                        .reuse(2)
                                        .detail("Chỉ giảm trên bia 666")
                                        .build()
                        )
                        .listBeer(new String[]{"123"})
                        .build())
                .block();

        voucherAPI.getAllVoucher(0, 100)
                .as(StepVerifier::create)
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_BIA_666");
                    assertThat(voucher.getDiscount()).isEqualTo(10);
                    assertThat(voucher.getDetail()).isEqualTo("Chỉ giảm trên bia 666");
                    assertThat(voucher.isFor_all_user()).isEqualTo(true);
                })
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_50k");
                    assertThat(voucher.getAmount()).isEqualTo(50000);
                    assertThat(voucher.getDetail()).isEqualTo("Giảm 50k trên 1 loại sản phẩm. Chỉ áp dụng cho ai nhận được.");
                    assertThat(voucher.isFor_all_user()).isEqualTo(false);
                })
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_30%");
                    assertThat(voucher.getDiscount()).isEqualTo(30);
                    assertThat(voucher.getDetail()).isEqualTo("Giảm 30% trên toàn bộ sản phẩm, chỉ áp dụng cho ai nhận được.");
                    assertThat(voucher.isFor_all_user()).isEqualTo(false);
                    assertThat(voucher.isFor_all_beer()).isEqualTo(true);
                })
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_5K");
                    assertThat(voucher.getAmount()).isEqualTo(5000);
                    assertThat(voucher.getDetail()).isEqualTo("Giảm 5k trên toàn bộ sản phẩm");
                    assertThat(voucher.isFor_all_user()).isEqualTo(true);
                    assertThat(voucher.isFor_all_beer()).isEqualTo(true);
                })
                .verifyComplete();

        voucherAPI.getVoucher("GIAM_BIA_666")
                .as(StepVerifier::create)
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_BIA_666");
                    assertThat(voucher.getDiscount()).isEqualTo(10);
                    assertThat(voucher.getDetail()).isEqualTo("Chỉ giảm trên bia 666");
                    assertThat(voucher.isFor_all_user()).isEqualTo(true);
                })
                .verifyComplete();

        voucherAPI.getDiscount("GIAM_50Kzk", "222222", "123")
                .as(StepVerifier::create)
                .verifyComplete();

        voucherAPI.getDiscount("GIAM_50K", "222222", "456")
                .as(StepVerifier::create)
                .verifyComplete();

        voucherAPI.getDiscount("GIAM_50K", "444444", "123")
                .as(StepVerifier::create)
                .verifyComplete();

        voucherAPI.getDiscount("GIAM_50k", "222222", "123")
                .as(StepVerifier::create)
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_50k");
                    assertThat(voucher.getAmount()).isEqualTo(50000);
                })
                .verifyComplete();

        voucherAPI.getDiscount("GIAM_5K", "444444", "456")
                .as(StepVerifier::create)
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_5K");
                    assertThat(voucher.getAmount()).isEqualTo(5000);
                })
                .verifyComplete();

        voucherAPI.getAllMyVoucher("222222")
                .as(StepVerifier::create)
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_BIA_666");
                    assertThat(voucher.getDiscount()).isEqualTo(10);
                    assertThat(voucher.getReuse()).isEqualTo(2);
                })
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_50k");
                    assertThat(voucher.getAmount()).isEqualTo(50000);
                    assertThat(voucher.getReuse()).isEqualTo(2);
                })
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_30%");
                    assertThat(voucher.getDiscount()).isEqualTo(30);
                    assertThat(voucher.getReuse()).isEqualTo(1);
                })
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_5K");
                    assertThat(voucher.getAmount()).isEqualTo(5000);
                    assertThat(voucher.getReuse()).isEqualTo(5);
                })
                .verifyComplete();

        voucherAPI.getAllMyVoucher("444444")
                .as(StepVerifier::create)
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_BIA_666");
                    assertThat(voucher.getDiscount()).isEqualTo(10);
                    assertThat(voucher.getReuse()).isEqualTo(2);
                })
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_5K");
                    assertThat(voucher.getAmount()).isEqualTo(5000);
                    assertThat(voucher.getReuse()).isEqualTo(5);
                })
                .verifyComplete();

        voucherAPI.comsumeVoucher("GIAM_50k", "444444", "456")
                .block();

        voucherAPI.comsumeVoucher("GIAM_5K", "444444", "456")
                .block();

        voucherAPI.comsumeVoucher("GIAM_5K", "444444", "456")
                .block();

        voucherAPI.comsumeVoucher("GIAM_5K", "444444", "456")
                .block();

        voucherAPI.comsumeVoucher("GIAM_5K", "444444", "456")
                .block();

        voucherAPI.getDiscount("GIAM_5K", "444444", "456")
                .as(StepVerifier::create)
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_5K");
                    assertThat(voucher.getAmount()).isEqualTo(5000);
                    assertThat(voucher.getReuse()).isEqualTo(1);
                })
                .verifyComplete();

        voucherAPI.comsumeVoucher("GIAM_5K", "444444", "456")
                .block();

        voucherAPI.getDiscount("GIAM_5K", "444444", "456")
                .as(StepVerifier::create)
                .verifyComplete();

        voucherAPI.comsumeVoucher("GIAM_5K", "555555", "456")
                .block();

        voucherAPI.getAllMyVoucher("555555")
                .as(StepVerifier::create)
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_BIA_666");
                    assertThat(voucher.getDiscount()).isEqualTo(10);
                    assertThat(voucher.getReuse()).isEqualTo(2);
                })
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_5K");
                    assertThat(voucher.getAmount()).isEqualTo(5000);
                    assertThat(voucher.getReuse()).isEqualTo(4);
                })
                .verifyComplete();

        voucherAPI.getAllMyVoucher("444444")
                .as(StepVerifier::create)
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_BIA_666");
                    assertThat(voucher.getDiscount()).isEqualTo(10);
                    assertThat(voucher.getReuse()).isEqualTo(2);
                })
                .verifyComplete();



        voucherAPI.createVoucher(
                VoucherData.builder()
                        .voucher(
                                com.example.heroku.model.Voucher
                                        .builder()
                                        .for_all_beer(true)
                                        .for_all_user(true)
                                        .voucher_second_id("GIAM_5K")
                                        .detail("Giảm 5k trên toàn bộ sản phẩm")
                                        .amount(5000)
                                        .reuse(8)
                                        .build()
                        )
                        .build())
                .block();

        voucherAPI.getDiscount("GIAM_5K", "444444", "456")
                .as(StepVerifier::create)
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_5K");
                    assertThat(voucher.getAmount()).isEqualTo(5000);
                    assertThat(voucher.getReuse()).isEqualTo(8);
                })
                .verifyComplete();

        voucherAPI.getAllMyVoucher("222222")
                .as(StepVerifier::create)
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_5K");
                    assertThat(voucher.getAmount()).isEqualTo(5000);
                    assertThat(voucher.getReuse()).isEqualTo(8);
                })
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_BIA_666");
                    assertThat(voucher.getDiscount()).isEqualTo(10);
                    assertThat(voucher.getReuse()).isEqualTo(2);
                })
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_50k");
                    assertThat(voucher.getAmount()).isEqualTo(50000);
                    assertThat(voucher.getReuse()).isEqualTo(2);
                })
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_30%");
                    assertThat(voucher.getDiscount()).isEqualTo(30);
                    assertThat(voucher.getReuse()).isEqualTo(1);
                })
                .verifyComplete();

        voucherAPI.getAllMyVoucher("444444")
                .as(StepVerifier::create)
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_5K");
                    assertThat(voucher.getAmount()).isEqualTo(5000);
                    assertThat(voucher.getReuse()).isEqualTo(8);
                })
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_BIA_666");
                    assertThat(voucher.getDiscount()).isEqualTo(10);
                    assertThat(voucher.getReuse()).isEqualTo(2);
                })
                .verifyComplete();

    }
}
