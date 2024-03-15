package com.example.heroku.response;


import com.example.heroku.model.statistics.BenifitByDateHour;
import com.example.heroku.model.statistics.BenifitByPaymentTransaction;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BenifitOfOrderAndPaymentTransactionByHour {
    private List<BenifitByDateHour> benifit_by_hour;
    private List<BenifitByDateHour> benifit_by_hour_transaction;

    private List<BenifitByPaymentTransaction> benifit_by_category_transaction;

    private float return_price;

    public BenifitOfOrderAndPaymentTransactionByHour setBenifitByHour(List<BenifitByDateHour> benifit) {
        benifit_by_hour = benifit;
        return this;
    }

    public BenifitOfOrderAndPaymentTransactionByHour setBenifitTransactionByHour(List<BenifitByDateHour> benifit) {
        benifit_by_hour_transaction = benifit;
        return this;
    }

    public BenifitOfOrderAndPaymentTransactionByHour setBenifitTransactionCategoryByHour(List<BenifitByPaymentTransaction> benifit) {
        benifit_by_category_transaction = benifit;
        return this;
    }

    public BenifitOfOrderAndPaymentTransactionByHour setReturnPrice(float return_price){
        this.return_price = return_price;
        return this;
    }
}
