package com.example.heroku.response;

import com.example.heroku.model.UserPackageDetail;
import com.example.heroku.request.beer.BeerSubmitData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageDataResponse extends UserPackageDetail {

    private List<ProductInPackageResponse> items;

    private BuyerData buyer;

    public static PackageDataResponse Empty() {
        return new PackageDataResponse();
    }

    public PackageDataResponse(UserPackageDetail s) {
        super.copy(s);
        items = new ArrayList<>();
    }

    public PackageDataResponse addItem(ProductInPackageResponse i) {
        items.add(i);
        return this;
    }

    public PackageDataResponse setBuyer(BuyerData buyer) {
        this.buyer = buyer;
        return this;
    }

    public PackageDataResponse setProductData(List<BeerSubmitData> listProduct) {

        Map<String, BeerSubmitData> lookup = new HashMap<>();
        for (BeerSubmitData p : listProduct) {
            BeerSubmitData.BeerUnit[] firstE = p.getListUnit();
            if (firstE == null || firstE.length == 0) {
                continue;
            }
            lookup.put(p.getBeerSecondID() + firstE[0].getBeer_unit_second_id(), p);
        }
        for (ProductInPackageResponse i : items) {
            BeerSubmitData productData = lookup.get(i.getProduct_second_id() + i.getProduct_unit_second_id());
            if (productData == null) {
                continue;
            }
            i.SetBeerData(productData);
        }
        return this;
    }
}
