package com.example.heroku.response;

import com.example.heroku.model.Buyer;
import com.example.heroku.model.statistics.BenifitByMonth;
import com.example.heroku.model.statistics.BenifitByProduct;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class BuyerStatictisData {
    private Buyer buyer;
    private List<BenifitByProduct> benifitByProducts;
    private BenifitByMonth benifitByMonth;
    private List<PackageDataResponse> packageDataResponses;

    public BuyerStatictisData addPackge(PackageDataResponse packageDataResponse) {
        if (packageDataResponses == null) {
            packageDataResponses = new ArrayList<>();
        }
        packageDataResponses.add(packageDataResponse);
        return this;
    }

    public BuyerStatictisData addProduct(BenifitByProduct benifitByProduct) {
        if (benifitByProducts == null) {
            benifitByProducts = new ArrayList<>();
        }
        benifitByProducts.add(benifitByProduct);
        return this;
    }
}
