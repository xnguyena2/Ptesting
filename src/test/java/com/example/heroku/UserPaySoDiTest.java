package com.example.heroku;

import com.example.heroku.model.UserPaySoDi;
import lombok.Builder;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class UserPaySoDiTest {

    com.example.heroku.services.UserPaySoDi userPaySoDiService;

    String group;

    @Test
    public void test() {
        AtomicReference<Long> id1 = new AtomicReference<>();
        AtomicReference<Long> id2 = new AtomicReference<>();

        UserPaySoDi payment = new UserPaySoDi();
        payment.setGroup_id(group);
        payment.setAmount(100.0f);
        payment.setNote("Test Note");
        payment.setPlan("Test Plan");
        payment.setBonus(10);

        userPaySoDiService.createTransaction(payment).block();

        payment.setAmount(10);

        userPaySoDiService.createTransaction(payment).block();

        userPaySoDiService.findByGroupId(group)
                .sort(Comparator.comparing(UserPaySoDi::getId))
                .as(StepVerifier::create)
                .consumeNextWith(userPaySoDi -> {
                    id2.set(userPaySoDi.getId());
                    assertThat(userPaySoDi.getGroup_id()).isEqualTo(group);
                    assertThat(userPaySoDi.getAmount()).isEqualTo(10);
                    assertThat(userPaySoDi.getNote()).isEqualTo("Test Note");
                    assertThat(userPaySoDi.getPlan()).isEqualTo("Test Plan");
                    assertThat(userPaySoDi.getBonus()).isEqualTo(10);
                })
                .consumeNextWith(userPaySoDi -> {
                    id1.set(userPaySoDi.getId());
                    assertThat(userPaySoDi.getGroup_id()).isEqualTo(group);
                    assertThat(userPaySoDi.getAmount()).isEqualTo(100.0f);
                    assertThat(userPaySoDi.getNote()).isEqualTo("Test Note");
                    assertThat(userPaySoDi.getPlan()).isEqualTo("Test Plan");
                    assertThat(userPaySoDi.getBonus()).isEqualTo(10);
                })
                .verifyComplete();



        userPaySoDiService.deleteByID(group, id1.get()).subscribe(deletedPayment -> {
        });


        userPaySoDiService.findByGroupId(group)
                .sort(Comparator.comparing(UserPaySoDi::getId))
                .as(StepVerifier::create)
                .consumeNextWith(userPaySoDi -> {
                    id2.set(userPaySoDi.getId());
                    assertThat(userPaySoDi.getGroup_id()).isEqualTo(group);
                    assertThat(userPaySoDi.getAmount()).isEqualTo(10);
                    assertThat(userPaySoDi.getNote()).isEqualTo("Test Note");
                    assertThat(userPaySoDi.getPlan()).isEqualTo("Test Plan");
                    assertThat(userPaySoDi.getBonus()).isEqualTo(10);
                })
                .verifyComplete();
    }
}
