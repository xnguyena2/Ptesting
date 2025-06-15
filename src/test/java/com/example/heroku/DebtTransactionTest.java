package com.example.heroku;

import com.example.heroku.model.DebtTransation;
import com.example.heroku.model.statistics.DebtOfBuyer;
import com.example.heroku.request.carousel.IDContainer;
import com.example.heroku.request.client.PackageID;
import com.example.heroku.request.client.UserID;
import lombok.Builder;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class DebtTransactionTest {

    com.example.heroku.services.DebtTransation debtTransation;

    String group;

    public void Test() {
        debtTransation.insertOrUpdate(DebtTransation.builder()
                .group_id(group)
                .transaction_second_id(group+"123")
                .action_id("2222")
                .transaction_type(DebtTransation.TType.INCOME)
                .category("ban hang")
                .money_source("tien mat")
                .amount(100000)
                .build()).block();
        debtTransation.insertOrUpdate(DebtTransation.builder()
                .group_id(group)
                .transaction_second_id(group+"234")
                .transaction_type(DebtTransation.TType.INCOME)
                .category("ban hang")
                .money_source("tien mat")
                .amount(55000)
                .build()).block();
        debtTransation.insertOrUpdate(DebtTransation.builder()
                .group_id(group)
                .transaction_second_id(group+"345")
                .action_id("2222")
                .transaction_type(DebtTransation.TType.OUTCOME)
                .category("mua hang")
                .money_source("tien mat")
                .amount(33000)
                .build()).block();

        debtTransation.getAllTransactionBettwen(PackageID.builder().group_id(group).from(LocalDateTime.parse("2023-11-01T00:00:00")).to(LocalDateTime.parse("2300-11-01T00:00:00")).build())
                .sort(Comparator.comparing(DebtTransation::getTransaction_second_id))
                .as(StepVerifier::create)
                .consumeNextWith(transation -> {
                    assertThat(transation.getGroup_id()).isEqualTo(group);
                    assertThat(transation.getTransaction_second_id()).isEqualTo(group+"123");
                    assertThat(transation.getAction_id()).isEqualTo("2222");
                    assertThat(transation.getAction_type()).isEqualTo(DebtTransation.ActionType.USER_PROPOSE);
                    assertThat(transation.getTransaction_type()).isEqualTo(DebtTransation.TType.INCOME);
                    assertThat(transation.getCategory()).isEqualTo("ban hang");
                    assertThat(transation.getMoney_source()).isEqualTo("tien mat");
                    assertThat(transation.getAmount()).isEqualTo(100000);
                })
                .consumeNextWith(transation -> {
                    assertThat(transation.getGroup_id()).isEqualTo(group);
                    assertThat(transation.getTransaction_second_id()).isEqualTo(group+"234");
                    assertThat(transation.getAction_id()).isNull();
                    assertThat(transation.getAction_type()).isEqualTo(DebtTransation.ActionType.USER_PROPOSE);
                    assertThat(transation.getTransaction_type()).isEqualTo(DebtTransation.TType.INCOME);
                    assertThat(transation.getCategory()).isEqualTo("ban hang");
                    assertThat(transation.getMoney_source()).isEqualTo("tien mat");
                    assertThat(transation.getAmount()).isEqualTo(55000);
                })
                .consumeNextWith(transation -> {
                    assertThat(transation.getGroup_id()).isEqualTo(group);
                    assertThat(transation.getAction_id()).isEqualTo("2222");
                    assertThat(transation.getAction_type()).isEqualTo(DebtTransation.ActionType.USER_PROPOSE);
                    assertThat(transation.getTransaction_second_id()).isEqualTo(group+"345");
                    assertThat(transation.getTransaction_type()).isEqualTo(DebtTransation.TType.OUTCOME);
                    assertThat(transation.getCategory()).isEqualTo("mua hang");
                    assertThat(transation.getMoney_source()).isEqualTo("tien mat");
                    assertThat(transation.getAmount()).isEqualTo(33000);
                })
                .verifyComplete();

        debtTransation.getAllTransactionByPackageID(IDContainer.builder().group_id(group).id("2222").build())
                .sort(Comparator.comparing(DebtTransation::getTransaction_second_id))
                .as(StepVerifier::create)
                .consumeNextWith(transation -> {
                    assertThat(transation.getGroup_id()).isEqualTo(group);
                    assertThat(transation.getTransaction_second_id()).isEqualTo(group+"123");
                    assertThat(transation.getAction_id()).isEqualTo("2222");
                    assertThat(transation.getAction_type()).isEqualTo(DebtTransation.ActionType.USER_PROPOSE);
                    assertThat(transation.getTransaction_type()).isEqualTo(DebtTransation.TType.INCOME);
                    assertThat(transation.getCategory()).isEqualTo("ban hang");
                    assertThat(transation.getMoney_source()).isEqualTo("tien mat");
                    assertThat(transation.getAmount()).isEqualTo(100000);
                })
                .consumeNextWith(transation -> {
                    assertThat(transation.getGroup_id()).isEqualTo(group);
                    assertThat(transation.getAction_id()).isEqualTo("2222");
                    assertThat(transation.getAction_type()).isEqualTo(DebtTransation.ActionType.USER_PROPOSE);
                    assertThat(transation.getTransaction_second_id()).isEqualTo(group+"345");
                    assertThat(transation.getTransaction_type()).isEqualTo(DebtTransation.TType.OUTCOME);
                    assertThat(transation.getCategory()).isEqualTo("mua hang");
                    assertThat(transation.getMoney_source()).isEqualTo("tien mat");
                    assertThat(transation.getAmount()).isEqualTo(33000);
                })
                .verifyComplete();

        debtTransation.delete(IDContainer.builder().group_id(group).id(group+"123").build()).block();

        debtTransation.deleteOfPackgeID(IDContainer.builder().group_id(group).build()).block();

        debtTransation.getAllTransactionBettwen(PackageID.builder().group_id(group).from(LocalDateTime.parse("2023-11-01T00:00:00")).to(LocalDateTime.parse("2300-11-01T00:00:00")).build())
                .sort(Comparator.comparing(DebtTransation::getTransaction_second_id))
                .as(StepVerifier::create)
                .consumeNextWith(transation -> {
                    assertThat(transation.getGroup_id()).isEqualTo(group);
                    assertThat(transation.getTransaction_second_id()).isEqualTo(group+"234");
                    assertThat(transation.getAction_id()).isNull();
                    assertThat(transation.getAction_type()).isEqualTo(DebtTransation.ActionType.USER_PROPOSE);
                    assertThat(transation.getTransaction_type()).isEqualTo(DebtTransation.TType.INCOME);
                    assertThat(transation.getCategory()).isEqualTo("ban hang");
                    assertThat(transation.getMoney_source()).isEqualTo("tien mat");
                    assertThat(transation.getAmount()).isEqualTo(55000);
                })
                .consumeNextWith(transation -> {
                    assertThat(transation.getGroup_id()).isEqualTo(group);
                    assertThat(transation.getAction_id()).isEqualTo("2222");
                    assertThat(transation.getAction_type()).isEqualTo(DebtTransation.ActionType.USER_PROPOSE);
                    assertThat(transation.getTransaction_second_id()).isEqualTo(group+"345");
                    assertThat(transation.getTransaction_type()).isEqualTo(DebtTransation.TType.OUTCOME);
                    assertThat(transation.getCategory()).isEqualTo("mua hang");
                    assertThat(transation.getMoney_source()).isEqualTo("tien mat");
                    assertThat(transation.getAmount()).isEqualTo(33000);
                })
                .verifyComplete();

        debtTransation.getAllCategory(IDContainer.builder().group_id(group).build())
                .as(StepVerifier::create)
                .expectNextCount(2)
                .verifyComplete();

        debtTransation.getAllTransactionByPackageID(IDContainer.builder().group_id(group).id("2222").build())
                .sort(Comparator.comparing(DebtTransation::getTransaction_second_id))
                .as(StepVerifier::create)
                .consumeNextWith(transation -> {
                    assertThat(transation.getGroup_id()).isEqualTo(group);
                    assertThat(transation.getAction_id()).isEqualTo("2222");
                    assertThat(transation.getAction_type()).isEqualTo(DebtTransation.ActionType.USER_PROPOSE);
                    assertThat(transation.getTransaction_second_id()).isEqualTo(group+"345");
                    assertThat(transation.getTransaction_type()).isEqualTo(DebtTransation.TType.OUTCOME);
                    assertThat(transation.getCategory()).isEqualTo("mua hang");
                    assertThat(transation.getMoney_source()).isEqualTo("tien mat");
                    assertThat(transation.getAmount()).isEqualTo(33000);
                })
                .verifyComplete();

        debtTransation.insertOrUpdate(DebtTransation.builder()
                .group_id(group)
                .transaction_second_id(group+"456")
                .transaction_type(DebtTransation.TType.OUTCOME)
                .device_id("iphone")
                .category("mua hang")
                .money_source("tien mat")
                .amount(33000)
                .build()).block();

        debtTransation.getAllTransaction(UserID.builder().group_id(group).page(0).size(10).build())
                .sort(Comparator.comparing(DebtTransation::getTransaction_second_id))
                .as(StepVerifier::create)
                .consumeNextWith(transation -> {
                    assertThat(transation.getGroup_id()).isEqualTo(group);
                    assertThat(transation.getTransaction_second_id()).isEqualTo(group+"234");
                    assertThat(transation.getAction_id()).isNull();
                    assertThat(transation.getAction_type()).isEqualTo(DebtTransation.ActionType.USER_PROPOSE);
                    assertThat(transation.getTransaction_type()).isEqualTo(DebtTransation.TType.INCOME);
                    assertThat(transation.getCategory()).isEqualTo("ban hang");
                    assertThat(transation.getMoney_source()).isEqualTo("tien mat");
                    assertThat(transation.getAmount()).isEqualTo(55000);
                })
                .consumeNextWith(transation -> {
                    assertThat(transation.getGroup_id()).isEqualTo(group);
                    assertThat(transation.getAction_id()).isEqualTo("2222");
                    assertThat(transation.getAction_type()).isEqualTo(DebtTransation.ActionType.USER_PROPOSE);
                    assertThat(transation.getTransaction_second_id()).isEqualTo(group+"345");
                    assertThat(transation.getTransaction_type()).isEqualTo(DebtTransation.TType.OUTCOME);
                    assertThat(transation.getCategory()).isEqualTo("mua hang");
                    assertThat(transation.getMoney_source()).isEqualTo("tien mat");
                    assertThat(transation.getAmount()).isEqualTo(33000);
                })
                .consumeNextWith(transation -> {
                    assertThat(transation.getGroup_id()).isEqualTo(group);
                    assertThat(transation.getAction_id()).isNull();
                    assertThat(transation.getAction_type()).isEqualTo(DebtTransation.ActionType.USER_PROPOSE);
                    assertThat(transation.getTransaction_second_id()).isEqualTo(group+"456");
                    assertThat(transation.getTransaction_type()).isEqualTo(DebtTransation.TType.OUTCOME);
                    assertThat(transation.getCategory()).isEqualTo("mua hang");
                    assertThat(transation.getDevice_id()).isEqualTo("iphone");
                    assertThat(transation.getMoney_source()).isEqualTo("tien mat");
                    assertThat(transation.getAmount()).isEqualTo(33000);
                })
                .verifyComplete();

        debtTransation.getAllTransaction(UserID.builder().group_id(group).after_id(1000).page(0).size(10).build())
                .sort(Comparator.comparing(DebtTransation::getTransaction_second_id))
                .as(StepVerifier::create)
                .consumeNextWith(transation -> {
                    assertThat(transation.getGroup_id()).isEqualTo(group);
                    assertThat(transation.getTransaction_second_id()).isEqualTo(group+"234");
                    assertThat(transation.getAction_id()).isNull();
                    assertThat(transation.getAction_type()).isEqualTo(DebtTransation.ActionType.USER_PROPOSE);
                    assertThat(transation.getTransaction_type()).isEqualTo(DebtTransation.TType.INCOME);
                    assertThat(transation.getCategory()).isEqualTo("ban hang");
                    assertThat(transation.getMoney_source()).isEqualTo("tien mat");
                    assertThat(transation.getAmount()).isEqualTo(55000);
                })
                .consumeNextWith(transation -> {
                    assertThat(transation.getGroup_id()).isEqualTo(group);
                    assertThat(transation.getAction_id()).isEqualTo("2222");
                    assertThat(transation.getAction_type()).isEqualTo(DebtTransation.ActionType.USER_PROPOSE);
                    assertThat(transation.getTransaction_second_id()).isEqualTo(group+"345");
                    assertThat(transation.getTransaction_type()).isEqualTo(DebtTransation.TType.OUTCOME);
                    assertThat(transation.getCategory()).isEqualTo("mua hang");
                    assertThat(transation.getMoney_source()).isEqualTo("tien mat");
                    assertThat(transation.getAmount()).isEqualTo(33000);
                })
                .consumeNextWith(transation -> {
                    assertThat(transation.getGroup_id()).isEqualTo(group);
                    assertThat(transation.getAction_id()).isNull();
                    assertThat(transation.getAction_type()).isEqualTo(DebtTransation.ActionType.USER_PROPOSE);
                    assertThat(transation.getTransaction_second_id()).isEqualTo(group+"456");
                    assertThat(transation.getTransaction_type()).isEqualTo(DebtTransation.TType.OUTCOME);
                    assertThat(transation.getCategory()).isEqualTo("mua hang");
                    assertThat(transation.getDevice_id()).isEqualTo("iphone");
                    assertThat(transation.getMoney_source()).isEqualTo("tien mat");
                    assertThat(transation.getAmount()).isEqualTo(33000);
                })
                .verifyComplete();

        debtTransation.getAllTransactionOfBuyer(UserID.builder().group_id(group).id("iphone").page(0).size(10).build())
                .sort(Comparator.comparing(DebtTransation::getTransaction_second_id))
                .as(StepVerifier::create)
                .consumeNextWith(transation -> {
                    assertThat(transation.getGroup_id()).isEqualTo(group);
                    assertThat(transation.getTransaction_second_id()).isEqualTo(group+"456");
                    assertThat(transation.getAction_id()).isNull();
                    assertThat(transation.getAction_type()).isEqualTo(DebtTransation.ActionType.USER_PROPOSE);
                    assertThat(transation.getTransaction_type()).isEqualTo(DebtTransation.TType.OUTCOME);
                    assertThat(transation.getCategory()).isEqualTo("mua hang");
                    assertThat(transation.getMoney_source()).isEqualTo("tien mat");
                    assertThat(transation.getAmount()).isEqualTo(33000);
                })
                .verifyComplete();

        debtTransation.getAllTransactionOfBuyer(UserID.builder().group_id(group).id("iphone").after_id(1000).page(0).size(10).build())
                .sort(Comparator.comparing(DebtTransation::getTransaction_second_id))
                .as(StepVerifier::create)
                .consumeNextWith(transation -> {
                    assertThat(transation.getGroup_id()).isEqualTo(group);
                    assertThat(transation.getTransaction_second_id()).isEqualTo(group+"456");
                    assertThat(transation.getAction_id()).isNull();
                    assertThat(transation.getAction_type()).isEqualTo(DebtTransation.ActionType.USER_PROPOSE);
                    assertThat(transation.getTransaction_type()).isEqualTo(DebtTransation.TType.OUTCOME);
                    assertThat(transation.getCategory()).isEqualTo("mua hang");
                    assertThat(transation.getMoney_source()).isEqualTo("tien mat");
                    assertThat(transation.getAmount()).isEqualTo(33000);
                })
                .verifyComplete();

        debtTransation.getDebtOfAllBuyer(IDContainer.builder().group_id(group).build())
                .sort(Comparator.comparing(DebtOfBuyer::getIn_come))
                .as(StepVerifier::create)
                .consumeNextWith(debtOfBuyer -> {
                    assertThat(debtOfBuyer.getGroup_id()).isNull();
                    assertThat(debtOfBuyer.getIn_come()).isEqualTo(0);
                    assertThat(debtOfBuyer.getOut_come()).isEqualTo(33000);
                    assertThat(debtOfBuyer.getDevice_id()).isNull();
                })
                .consumeNextWith(debtOfBuyer -> {
                    assertThat(debtOfBuyer.getGroup_id()).isNull();
                    assertThat(debtOfBuyer.getIn_come()).isEqualTo(55000);
                    assertThat(debtOfBuyer.getOut_come()).isEqualTo(33000);
                    assertThat(debtOfBuyer.getDevice_id()).isNull();
                })
                .verifyComplete();

        debtTransation.deleteOfPackgeID(IDContainer.builder().group_id(group).id("2222").build())
                .block();

        debtTransation.getAllTransaction(UserID.builder().group_id(group).after_id(1000).page(0).size(10).build())
                .sort(Comparator.comparing(DebtTransation::getTransaction_second_id))
                .as(StepVerifier::create)
                .consumeNextWith(transation -> {
                    assertThat(transation.getGroup_id()).isEqualTo(group);
                    assertThat(transation.getTransaction_second_id()).isEqualTo(group+"234");
                    assertThat(transation.getAction_id()).isNull();
                    assertThat(transation.getAction_type()).isEqualTo(DebtTransation.ActionType.USER_PROPOSE);
                    assertThat(transation.getTransaction_type()).isEqualTo(DebtTransation.TType.INCOME);
                    assertThat(transation.getCategory()).isEqualTo("ban hang");
                    assertThat(transation.getMoney_source()).isEqualTo("tien mat");
                    assertThat(transation.getAmount()).isEqualTo(55000);
                })
                .consumeNextWith(transation -> {
                    assertThat(transation.getGroup_id()).isEqualTo(group);
                    assertThat(transation.getAction_id()).isNull();
                    assertThat(transation.getAction_type()).isEqualTo(DebtTransation.ActionType.USER_PROPOSE);
                    assertThat(transation.getTransaction_second_id()).isEqualTo(group+"456");
                    assertThat(transation.getTransaction_type()).isEqualTo(DebtTransation.TType.OUTCOME);
                    assertThat(transation.getCategory()).isEqualTo("mua hang");
                    assertThat(transation.getDevice_id()).isEqualTo("iphone");
                    assertThat(transation.getMoney_source()).isEqualTo("tien mat");
                    assertThat(transation.getAmount()).isEqualTo(33000);
                })
                .verifyComplete();

        debtTransation.getDebtOfAllBuyer(IDContainer.builder().group_id(group).build())
                .sort(Comparator.comparing(DebtOfBuyer::getIn_come))
                .as(StepVerifier::create)
                .consumeNextWith(debtOfBuyer -> {
                    assertThat(debtOfBuyer.getGroup_id()).isNull();
                    assertThat(debtOfBuyer.getIn_come()).isEqualTo(0);
                    assertThat(debtOfBuyer.getOut_come()).isEqualTo(33000);
                    assertThat(debtOfBuyer.getDevice_id()).isNull();
                })
                .consumeNextWith(debtOfBuyer -> {
                    assertThat(debtOfBuyer.getGroup_id()).isNull();
                    assertThat(debtOfBuyer.getIn_come()).isEqualTo(55000);
                    assertThat(debtOfBuyer.getOut_come()).isEqualTo(0);
                    assertThat(debtOfBuyer.getDevice_id()).isNull();
                })
                .verifyComplete();

        debtTransation.getDebtOfAllBuyerInner(IDContainer.builder().group_id(group).build())
                .sort(Comparator.comparing(DebtOfBuyer::getIn_come))
                .as(StepVerifier::create)
                .verifyComplete();

    }
}
