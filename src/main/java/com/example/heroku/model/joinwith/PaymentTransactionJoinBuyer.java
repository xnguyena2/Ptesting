package com.example.heroku.model.joinwith;

import com.example.heroku.model.*;
import com.example.heroku.request.transaction.PaymentTransactionBuyer;
import com.example.heroku.status.ActiveStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTransactionJoinBuyer extends PaymentTransation {


    // Buyer


    protected String buyer_group_id;
    protected LocalDateTime buyer_createat;

    private String device_id;

    private String reciver_fullname;

    private String phone_number_clean;

    private String phone_number;

    private String reciver_address;

    private int region_id;

    private int district_id;

    private int ward_id;

    private float real_price;

    private float total_price;

    private float ship_price;

    private float discount;

    private int point;

    private String meta_search;

    private ActiveStatus buyer_status;

    public Buyer createBuyer() {
        if (buyer_group_id == null) {
            return null;
        }
        return Buyer.builder()
                .group_id(this.buyer_group_id)
                .createat(this.buyer_createat)
                .device_id(this.device_id)
                .reciver_fullname(this.reciver_fullname)
                .phone_number_clean(this.phone_number_clean)
                .phone_number(this.phone_number)
                .reciver_address(this.reciver_address)
                .region_id(this.region_id)
                .district_id(this.district_id)
                .ward_id(this.ward_id)
                .real_price(this.real_price)
                .total_price(this.total_price)
                .ship_price(this.ship_price)
                .discount(this.discount)
                .point(this.point)
                .meta_search(this.meta_search)
                .status(this.buyer_status)
                .build();
    }

    public static PaymentTransactionBuyer GeneratePaymentTransactionBuyer(PaymentTransactionJoinBuyer paymentTransactionJoinBuyer) {
        return new PaymentTransactionBuyer(paymentTransactionJoinBuyer, paymentTransactionJoinBuyer.createBuyer());
    }

}
