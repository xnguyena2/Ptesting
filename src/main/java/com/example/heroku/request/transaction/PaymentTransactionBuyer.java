package com.example.heroku.request.transaction;

import com.example.heroku.model.Buyer;
import com.example.heroku.model.PaymentTransation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder()
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTransactionBuyer extends PaymentTransation {

    private Buyer buyer;

    public PaymentTransactionBuyer(PaymentTransation s, Buyer buyer) {
        super(s);
        this.buyer = buyer;
    }

    public PaymentTransactionBuyer AutoFill() {
        return (PaymentTransactionBuyer) super.AutoFill();
    }

}
