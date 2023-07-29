package com.example.heroku;

import com.example.heroku.request.beer.BeerSubmitData;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.voucher.VoucherData;
import com.example.heroku.services.Voucher;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class VoucherTest {

    @Autowired
    Voucher voucherAPI;

    public void VoucherTest() {

        voucherAPI.createVoucher(
                        VoucherData.builder()
                                .group_id(Config.group)
                                .for_all_product(true)
                                .for_all_user(true)
                                .voucher_second_id("GIAM_5K")
                                .detail("Giảm 5k trên toàn bộ sản phẩm")
                                .amount(5000)
                                .reuse(5)
                                .build())
                .block();

        voucherAPI.createVoucher(
                        VoucherData.builder()
                                .group_id(Config.group)
                                .for_all_product(true)
                                .voucher_second_id("GIAM_30%")
                                .discount(30)
                                .reuse(1)
                                .detail("Giảm 30% trên toàn bộ sản phẩm, chỉ áp dụng cho ai nhận được.")
                                .listUser(new String[]{"222222"})
                                .build())
                .block();

        voucherAPI.createVoucher(
                        VoucherData.builder()
                                .group_id(Config.group)
                                .voucher_second_id("GIAM_50k")
                                .detail("Giảm 50k trên 1 loại sản phẩm. Chỉ áp dụng cho ai nhận được.")
                                .amount(50000)
                                .reuse(2)
                                .listBeer(new String[]{"123", "123", "123"})
                                .listUser(new String[]{"222222", "222222", "222222", "333333"})
                                .build())
                .block();

        voucherAPI.createVoucher(
                        VoucherData.builder()
                                .group_id(Config.group)
                                .for_all_user(true)
                                .voucher_second_id("GIAM_BIA_666")
                                .discount(10)
                                .reuse(2)
                                .detail("Chỉ giảm trên bia 666")
                                .listBeer(new String[]{"123"})
                                .build())
                .block();

        voucherAPI.getAllVoucher(SearchQuery.builder().page(0).size(100).group_id(Config.group).build())
                .sort(Comparator.comparing(VoucherData::getVoucher_second_id))
                .as(StepVerifier::create)
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_30%");
                    assertThat(voucher.getDiscount()).isEqualTo(30);
                    assertThat(voucher.getDetail()).isEqualTo("Giảm 30% trên toàn bộ sản phẩm, chỉ áp dụng cho ai nhận được.");
                    assertThat(voucher.isFor_all_user()).isEqualTo(false);
                    assertThat(voucher.isFor_all_product()).isEqualTo(true);
                })
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_50k");
                    assertThat(voucher.getAmount()).isEqualTo(50000);
                    assertThat(voucher.getDetail()).isEqualTo("Giảm 50k trên 1 loại sản phẩm. Chỉ áp dụng cho ai nhận được.");
                    assertThat(voucher.isFor_all_user()).isEqualTo(false);
                })
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_5K");
                    assertThat(voucher.getAmount()).isEqualTo(5000);
                    assertThat(voucher.getDetail()).isEqualTo("Giảm 5k trên toàn bộ sản phẩm");
                    assertThat(voucher.isFor_all_user()).isEqualTo(true);
                    assertThat(voucher.isFor_all_product()).isEqualTo(true);
                })
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_BIA_666");
                    assertThat(voucher.getDiscount()).isEqualTo(10);
                    assertThat(voucher.getDetail()).isEqualTo("Chỉ giảm trên bia 666");
                    assertThat(voucher.isFor_all_user()).isEqualTo(true);
                })
                .verifyComplete();

        voucherAPI.getVoucher(Config.group, "GIAM_BIA_666")
                .as(StepVerifier::create)
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_BIA_666");
                    assertThat(voucher.getDiscount()).isEqualTo(10);
                    assertThat(voucher.getDetail()).isEqualTo("Chỉ giảm trên bia 666");
                    assertThat(voucher.isFor_all_user()).isEqualTo(true);
                })
                .verifyComplete();

        voucherAPI.getDiscount(Config.group, "GIAM_50Kzk", "222222", "123")
                .as(StepVerifier::create)
                .verifyComplete();

        voucherAPI.getDiscount(Config.group, "GIAM_50K", "222222", "456")
                .as(StepVerifier::create)
                .verifyComplete();

        voucherAPI.getDiscount(Config.group, "GIAM_50K", "444444", "123")
                .as(StepVerifier::create)
                .verifyComplete();

        voucherAPI.getDiscount(Config.group, "GIAM_50k", "222222", "123")
                .as(StepVerifier::create)
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_50k");
                    assertThat(voucher.getAmount()).isEqualTo(50000);
                })
                .verifyComplete();

        voucherAPI.getDiscount(Config.group, "GIAM_5K", "444444", "456")
                .as(StepVerifier::create)
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_5K");
                    assertThat(voucher.getAmount()).isEqualTo(5000);
                })
                .verifyComplete();

        voucherAPI.getAllMyVoucher(Config.group, "222222")
                .sort(Comparator.comparing(com.example.heroku.model.Voucher::getVoucher_second_id))
                .as(StepVerifier::create)
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_30%");
                    assertThat(voucher.getDiscount()).isEqualTo(30);
                    assertThat(voucher.getReuse()).isEqualTo(1);
                })
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_50k");
                    assertThat(voucher.getAmount()).isEqualTo(50000);
                    assertThat(voucher.getReuse()).isEqualTo(2);
                })
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_5K");
                    assertThat(voucher.getAmount()).isEqualTo(5000);
                    assertThat(voucher.getReuse()).isEqualTo(5);
                })
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_BIA_666");
                    assertThat(voucher.getDiscount()).isEqualTo(10);
                    assertThat(voucher.getReuse()).isEqualTo(2);
                })
                .verifyComplete();

        voucherAPI.getAllMyVoucher(Config.group, "444444")
                .sort(Comparator.comparing(com.example.heroku.model.Voucher::getVoucher_second_id))
                .as(StepVerifier::create)
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_5K");
                    assertThat(voucher.getAmount()).isEqualTo(5000);
                    assertThat(voucher.getReuse()).isEqualTo(5);
                })
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_BIA_666");
                    assertThat(voucher.getDiscount()).isEqualTo(10);
                    assertThat(voucher.getReuse()).isEqualTo(2);
                })
                .verifyComplete();

        voucherAPI.comsumeVoucher(Config.group, "GIAM_50k", "444444", "456")
                .block();

        voucherAPI.comsumeVoucher(Config.group, "GIAM_5K", "444444", "456")
                .block();

        voucherAPI.comsumeVoucher(Config.group, "GIAM_5K", "444444", "456")
                .block();

        voucherAPI.comsumeVoucher(Config.group, "GIAM_5K", "444444", "456")
                .block();

        voucherAPI.comsumeVoucher(Config.group, "GIAM_5K", "444444", "456")
                .block();

        voucherAPI.getDiscount(Config.group, "GIAM_5K", "444444", "456")
                .as(StepVerifier::create)
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_5K");
                    assertThat(voucher.getAmount()).isEqualTo(5000);
                    assertThat(voucher.getReuse()).isEqualTo(1);
                })
                .verifyComplete();

        voucherAPI.comsumeVoucher(Config.group, "GIAM_5K", "444444", "456")
                .block();

        voucherAPI.getDiscount(Config.group, "GIAM_5K", "444444", "456")
                .as(StepVerifier::create)
                .verifyComplete();

        voucherAPI.comsumeVoucher(Config.group, "GIAM_5K", "555555", "456")
                .block();

        voucherAPI.getAllMyVoucher(Config.group, "555555")
                .sort(Comparator.comparing(com.example.heroku.model.Voucher::getVoucher_second_id))
                .as(StepVerifier::create)
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_5K");
                    assertThat(voucher.getAmount()).isEqualTo(5000);
                    assertThat(voucher.getReuse()).isEqualTo(4);
                })
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_BIA_666");
                    assertThat(voucher.getDiscount()).isEqualTo(10);
                    assertThat(voucher.getReuse()).isEqualTo(2);
                })
                .verifyComplete();

        voucherAPI.getAllMyVoucher(Config.group, "444444")
                .as(StepVerifier::create)
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_BIA_666");
                    assertThat(voucher.getDiscount()).isEqualTo(10);
                    assertThat(voucher.getReuse()).isEqualTo(2);
                })
                .verifyComplete();


        voucherAPI.createVoucher(
                        VoucherData.builder()
                                .group_id(Config.group)
                                .for_all_product(true)
                                .for_all_user(true)
                                .voucher_second_id("GIAM_5K")
                                .detail("Giảm 5k trên toàn bộ sản phẩm")
                                .amount(5000)
                                .reuse(8)
                                .build())
                .block();

        voucherAPI.getDiscount(Config.group, "GIAM_5K", "444444", "456")
                .as(StepVerifier::create)
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_5K");
                    assertThat(voucher.getAmount()).isEqualTo(5000);
                    assertThat(voucher.getReuse()).isEqualTo(8);
                })
                .verifyComplete();

        voucherAPI.getAllMyVoucher(Config.group, "222222")
                .sort(Comparator.comparing(com.example.heroku.model.Voucher::getVoucher_second_id))
                .as(StepVerifier::create)
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_30%");
                    assertThat(voucher.getDiscount()).isEqualTo(30);
                    assertThat(voucher.getReuse()).isEqualTo(1);
                })
                .consumeNextWith(voucher -> {
                    assertThat(voucher.getVoucher_second_id()).isEqualTo("GIAM_50k");
                    assertThat(voucher.getAmount()).isEqualTo(50000);
                    assertThat(voucher.getReuse()).isEqualTo(2);
                })
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

        voucherAPI.getAllMyVoucher(Config.group, "444444")
                .sort(Comparator.comparing(com.example.heroku.model.Voucher::getVoucher_second_id))
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
