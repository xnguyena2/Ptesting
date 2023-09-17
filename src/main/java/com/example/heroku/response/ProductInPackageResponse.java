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
        BeerSubmitData.BeerUnit beerUnit = null;
        for (BeerSubmitData.BeerUnit unit :
                b.getListUnit()) {
            if (unit.getBeer_unit_second_id().equals(this.getProduct_unit_second_id())) {
                beerUnit = unit;
            }
        }
        b.setListUnit(new BeerSubmitData.BeerUnit[]{ beerUnit });
        this.beerSubmitData = b;
        return this;
    }
}
