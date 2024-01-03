package com.example.heroku;

import com.example.heroku.model.PaymentTransation;
import com.example.heroku.model.Store;
import com.example.heroku.request.beer.BeerSubmitData;
import com.example.heroku.request.carousel.IDContainer;
import com.example.heroku.request.client.PackageID;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.sql.Timestamp;
import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class PaymentTransactionTest {

    com.example.heroku.services.PaymentTransation paymentTransation;

    String group;

    public void Test() {
        paymentTransation.insertOrUpdate(PaymentTransation.builder()
                        .group_id(group)
                        .package_second_id("2222")
                        .transaction_second_id(group+"123")
                        .transaction_type(PaymentTransation.TType.INCOME)
                        .category("ban hang")
                        .money_source("tien mat")
                        .amount(100000)
                .build()).block();
        paymentTransation.insertOrUpdate(PaymentTransation.builder()
                .group_id(group)
                .transaction_second_id(group+"234")
                .transaction_type(PaymentTransation.TType.INCOME)
                .category("ban hang")
                .money_source("tien mat")
                .amount(55000)
                .build()).block();
        paymentTransation.insertOrUpdate(PaymentTransation.builder()
                .group_id(group)
                .package_second_id("2222")
                .transaction_second_id(group+"345")
                .transaction_type(PaymentTransation.TType.OUTCOME)
                .category("mua hang")
                .money_source("tien mat")
                .amount(33000)
                .build()).block();

        paymentTransation.getAllTransactionBettwen(PackageID.builder().group_id(group).from(Timestamp.valueOf("2023-11-01 00:00:00")).to(Timestamp.valueOf("2300-11-01 00:00:00")).build())
                .sort(Comparator.comparing(PaymentTransation::getTransaction_second_id))
                .as(StepVerifier::create)
                .consumeNextWith(transation -> {
                    assertThat(transation.getGroup_id()).isEqualTo(group);
                    assertThat(transation.getTransaction_second_id()).isEqualTo(group+"123");
                    assertThat(transation.getPackage_second_id()).isEqualTo("2222");
                    assertThat(transation.getTransaction_type()).isEqualTo(PaymentTransation.TType.INCOME);
                    assertThat(transation.getCategory()).isEqualTo("ban hang");
                    assertThat(transation.getMoney_source()).isEqualTo("tien mat");
                    assertThat(transation.getAmount()).isEqualTo(100000);
                })
                .consumeNextWith(transation -> {
                    assertThat(transation.getGroup_id()).isEqualTo(group);
                    assertThat(transation.getTransaction_second_id()).isEqualTo(group+"234");
                    assertThat(transation.getPackage_second_id()).isNull();
                    assertThat(transation.getTransaction_type()).isEqualTo(PaymentTransation.TType.INCOME);
                    assertThat(transation.getCategory()).isEqualTo("ban hang");
                    assertThat(transation.getMoney_source()).isEqualTo("tien mat");
                    assertThat(transation.getAmount()).isEqualTo(55000);
                })
                .consumeNextWith(transation -> {
                    assertThat(transation.getGroup_id()).isEqualTo(group);
                    assertThat(transation.getPackage_second_id()).isEqualTo("2222");
                    assertThat(transation.getTransaction_second_id()).isEqualTo(group+"345");
                    assertThat(transation.getTransaction_type()).isEqualTo(PaymentTransation.TType.OUTCOME);
                    assertThat(transation.getCategory()).isEqualTo("mua hang");
                    assertThat(transation.getMoney_source()).isEqualTo("tien mat");
                    assertThat(transation.getAmount()).isEqualTo(33000);
                })
                .verifyComplete();

        paymentTransation.getAllTransactionByPackageID(IDContainer.builder().group_id(group).id("2222").build())
                .sort(Comparator.comparing(PaymentTransation::getTransaction_second_id))
                .as(StepVerifier::create)
                .consumeNextWith(transation -> {
                    assertThat(transation.getGroup_id()).isEqualTo(group);
                    assertThat(transation.getTransaction_second_id()).isEqualTo(group+"123");
                    assertThat(transation.getPackage_second_id()).isEqualTo("2222");
                    assertThat(transation.getTransaction_type()).isEqualTo(PaymentTransation.TType.INCOME);
                    assertThat(transation.getCategory()).isEqualTo("ban hang");
                    assertThat(transation.getMoney_source()).isEqualTo("tien mat");
                    assertThat(transation.getAmount()).isEqualTo(100000);
                })
                .consumeNextWith(transation -> {
                    assertThat(transation.getGroup_id()).isEqualTo(group);
                    assertThat(transation.getPackage_second_id()).isEqualTo("2222");
                    assertThat(transation.getTransaction_second_id()).isEqualTo(group+"345");
                    assertThat(transation.getTransaction_type()).isEqualTo(PaymentTransation.TType.OUTCOME);
                    assertThat(transation.getCategory()).isEqualTo("mua hang");
                    assertThat(transation.getMoney_source()).isEqualTo("tien mat");
                    assertThat(transation.getAmount()).isEqualTo(33000);
                })
                .verifyComplete();

        paymentTransation.delete(IDContainer.builder().group_id(group).id(group+"123").build()).block();

        paymentTransation.deleteOfPackgeID(IDContainer.builder().group_id(group).build()).block();

        paymentTransation.getAllTransactionBettwen(PackageID.builder().group_id(group).from(Timestamp.valueOf("2023-11-01 00:00:00")).to(Timestamp.valueOf("2300-11-01 00:00:00")).build())
                .sort(Comparator.comparing(PaymentTransation::getTransaction_second_id))
                .as(StepVerifier::create)
                .consumeNextWith(transation -> {
                    assertThat(transation.getGroup_id()).isEqualTo(group);
                    assertThat(transation.getTransaction_second_id()).isEqualTo(group+"234");
                    assertThat(transation.getPackage_second_id()).isNull();
                    assertThat(transation.getTransaction_type()).isEqualTo(PaymentTransation.TType.INCOME);
                    assertThat(transation.getCategory()).isEqualTo("ban hang");
                    assertThat(transation.getMoney_source()).isEqualTo("tien mat");
                    assertThat(transation.getAmount()).isEqualTo(55000);
                })
                .consumeNextWith(transation -> {
                    assertThat(transation.getGroup_id()).isEqualTo(group);
                    assertThat(transation.getPackage_second_id()).isEqualTo("2222");
                    assertThat(transation.getTransaction_second_id()).isEqualTo(group+"345");
                    assertThat(transation.getTransaction_type()).isEqualTo(PaymentTransation.TType.OUTCOME);
                    assertThat(transation.getCategory()).isEqualTo("mua hang");
                    assertThat(transation.getMoney_source()).isEqualTo("tien mat");
                    assertThat(transation.getAmount()).isEqualTo(33000);
                })
                .verifyComplete();

        paymentTransation.getAllTransactionByPackageID(IDContainer.builder().group_id(group).id("2222").build())
                .sort(Comparator.comparing(PaymentTransation::getTransaction_second_id))
                .as(StepVerifier::create)
                .consumeNextWith(transation -> {
                    assertThat(transation.getGroup_id()).isEqualTo(group);
                    assertThat(transation.getPackage_second_id()).isEqualTo("2222");
                    assertThat(transation.getTransaction_second_id()).isEqualTo(group+"345");
                    assertThat(transation.getTransaction_type()).isEqualTo(PaymentTransation.TType.OUTCOME);
                    assertThat(transation.getCategory()).isEqualTo("mua hang");
                    assertThat(transation.getMoney_source()).isEqualTo("tien mat");
                    assertThat(transation.getAmount()).isEqualTo(33000);
                })
                .verifyComplete();
    }
}
