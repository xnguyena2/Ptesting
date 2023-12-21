package com.example.heroku.response;

import com.example.heroku.model.UserPackage;
import com.example.heroku.request.beer.BeerSubmitData;
import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInPackageResponse extends UserPackage {

    public ProductInPackageResponse(UserPackage s) {
        super(s);
    }

    private BeerSubmitData beerSubmitData;

    public ProductInPackageResponse SetBeerData(BeerSubmitData b) {
        if (b.getBeerSecondID() != null && !b.getBeerSecondID().isEmpty()) {
            this.beerSubmitData = b;
        }
        return this;
    }
}
