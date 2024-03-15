package com.example.heroku.response;


import com.example.heroku.model.statistics.BenifitByDate;
import com.example.heroku.model.statistics.BenifitByPaymentTransaction;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BenifitOfOrderAndPaymentTransactionByDate {
    private List<BenifitByDate> benifit_by_date;
    private List<BenifitByDate> benifit_by_date_transaction;

    private List<BenifitByPaymentTransaction> benifit_by_category_transaction;

    private float return_price;

    public BenifitOfOrderAndPaymentTransactionByDate setBenifitByDate(List<BenifitByDate> benifit) {
        benifit_by_date = benifit;
        return this;
    }

    public BenifitOfOrderAndPaymentTransactionByDate setBenifitTransactionByDate(List<BenifitByDate> benifit) {
        benifit_by_date_transaction = benifit;
        return this;
    }

    public BenifitOfOrderAndPaymentTransactionByDate setBenifitTransactionCategoryByDate(List<BenifitByPaymentTransaction> benifit) {
        benifit_by_category_transaction = benifit;
        return this;
    }

    public BenifitOfOrderAndPaymentTransactionByDate setReturnPrice(float return_price){
        this.return_price = return_price;
        return this;
    }
}
