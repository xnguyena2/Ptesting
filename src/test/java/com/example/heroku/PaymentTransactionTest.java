package com.example.heroku;

import com.example.heroku.model.PaymentTransation;
import com.example.heroku.model.Store;
import com.example.heroku.model.UserPackageDetail;
import com.example.heroku.model.statistics.BenifitByDate;
import com.example.heroku.model.statistics.BenifitByDateHour;
import com.example.heroku.model.statistics.BenifitByPaymentTransaction;
import com.example.heroku.request.beer.BeerSubmitData;
import com.example.heroku.request.carousel.IDContainer;
import com.example.heroku.request.client.PackageID;
import com.example.heroku.services.StatisticServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class PaymentTransactionTest {


    StatisticServices statisticServices;


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

        paymentTransation.getAllCategory(IDContainer.builder().group_id(group).build())
                .as(StepVerifier::create)
                .expectNextCount(2)
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

        paymentTransation.insertOrUpdate(PaymentTransation.builder()
                .group_id(group)
                .transaction_second_id(group+"456")
                .transaction_type(PaymentTransation.TType.OUTCOME)
                .category("mua hang")
                .money_source("tien mat")
                .amount(33000)
                .build()).block();

        statisticServices.getBenifitOfPaymentByDateStatictis(PackageID.builder().group_id(group).from(Timestamp.valueOf("2023-11-01 00:00:00")).to(Timestamp.valueOf("2300-11-01 00:00:00")).status(UserPackageDetail.Status.CREATE).build())
                .as(StepVerifier::create)
                .consumeNextWith(data -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(data));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(data.getReturn_price()).isEqualTo(0);
                    assertThat(data.getBenifit_by_date().size()).isEqualTo(1);
                    assertThat(data.getBenifit_by_date_transaction().size()).isEqualTo(1);
                    assertThat(data.getBenifit_by_category_transaction().size()).isEqualTo(2);


                    BenifitByDate benifitByDate = data.getBenifit_by_date().get(0);

                    BenifitByDate benifitTransactionByDate = data.getBenifit_by_date_transaction().get(0);
                    assertThat(benifitTransactionByDate.getRevenue()).isEqualTo(55000);
                    assertThat(benifitTransactionByDate.getCost()).isEqualTo(33000);


                    assertThat(benifitByDate.getCost()).isEqualTo(0);
                    assertThat(benifitByDate.getProfit()).isEqualTo(0);
                    assertThat(benifitByDate.getRevenue()).isEqualTo(0);
                    assertThat(benifitByDate.getPrice()).isEqualTo(1);
                    assertThat(benifitByDate.getCount()).isEqualTo(1);
                    assertThat(benifitByDate.getBuyer()).isEqualTo(1);
                    assertThat(benifitByDate.getShip_price()).isEqualTo(20000);
                    assertThat(benifitByDate.getDiscount_by_point()).isEqualTo(0);
                    assertThat(benifitByDate.getDiscount_promotional()).isEqualTo(0);
                    assertThat(benifitByDate.getAdditional_fee()).isEqualTo(0);
                    assertThat(data.getReturn_price()).isEqualTo(0);
                    assertThat(new DecimalFormat("0.0").format(benifitByDate.getDiscount())).isEqualTo("10000.1");

                    data.getBenifit_by_category_transaction().sort(Comparator.comparing(BenifitByPaymentTransaction::getCategory));

                    BenifitByPaymentTransaction selling = data.getBenifit_by_category_transaction().get(0);
                    BenifitByPaymentTransaction buying = data.getBenifit_by_category_transaction().get(1);


                    assertThat(selling.getCategory()).isEqualTo("ban hang");
                    assertThat(selling.getRevenue()).isEqualTo(55000);
                    assertThat(selling.getCost()).isEqualTo(0);

                    assertThat(buying.getCategory()).isEqualTo("mua hang");
                    assertThat(buying.getRevenue()).isEqualTo(0);
                    assertThat(buying.getCost()).isEqualTo(33000);
                })
                .verifyComplete();

        statisticServices.getBenifitOfPaymentByHourStatictis(PackageID.builder().group_id(group).from(Timestamp.valueOf("2023-11-01 00:00:00")).to(Timestamp.valueOf("2300-11-01 00:00:00")).status(UserPackageDetail.Status.CREATE).build())
                .as(StepVerifier::create)
                .consumeNextWith(data -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(data));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(data.getReturn_price()).isEqualTo(0);
                    assertThat(data.getBenifit_by_hour().size()).isEqualTo(1);
                    assertThat(data.getBenifit_by_hour_transaction().size()).isEqualTo(1);
                    assertThat(data.getBenifit_by_category_transaction().size()).isEqualTo(2);


                    BenifitByDateHour benifitByDate = data.getBenifit_by_hour().get(0);

                    BenifitByDateHour benifitTransactionByDate = data.getBenifit_by_hour_transaction().get(0);
                    assertThat(benifitTransactionByDate.getRevenue()).isEqualTo(55000);
                    assertThat(benifitTransactionByDate.getCost()).isEqualTo(33000);


                    assertThat(benifitByDate.getCost()).isEqualTo(0);
                    assertThat(benifitByDate.getProfit()).isEqualTo(0);
                    assertThat(benifitByDate.getRevenue()).isEqualTo(0);
                    assertThat(benifitByDate.getPrice()).isEqualTo(1);
                    assertThat(benifitByDate.getCount()).isEqualTo(1);
                    assertThat(benifitByDate.getBuyer()).isEqualTo(1);
                    assertThat(benifitByDate.getShip_price()).isEqualTo(20000);
                    assertThat(benifitByDate.getDiscount_by_point()).isEqualTo(0);
                    assertThat(benifitByDate.getDiscount_promotional()).isEqualTo(0);
                    assertThat(benifitByDate.getAdditional_fee()).isEqualTo(0);
                    assertThat(data.getReturn_price()).isEqualTo(0);
                    assertThat(new DecimalFormat("0.0").format(benifitByDate.getDiscount())).isEqualTo("10000.1");

                    data.getBenifit_by_category_transaction().sort(Comparator.comparing(BenifitByPaymentTransaction::getCategory));

                    BenifitByPaymentTransaction selling = data.getBenifit_by_category_transaction().get(0);
                    BenifitByPaymentTransaction buying = data.getBenifit_by_category_transaction().get(1);


                    assertThat(selling.getCategory()).isEqualTo("ban hang");
                    assertThat(selling.getRevenue()).isEqualTo(55000);
                    assertThat(selling.getCost()).isEqualTo(0);

                    assertThat(buying.getCategory()).isEqualTo("mua hang");
                    assertThat(buying.getRevenue()).isEqualTo(0);
                    assertThat(buying.getCost()).isEqualTo(33000);
                })
                .verifyComplete();
    }
}
