package com.example.heroku.response;

import com.example.heroku.model.BeerUnit;
import com.example.heroku.model.UserPackage;
import com.example.heroku.request.beer.BeerSubmitData;
import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackgeResponse extends UserPackage {

    public PackgeResponse(UserPackage s) {
        super(s);
    }

    private BeerSubmitData beerSubmitData;

    public PackgeResponse SetBeerData(BeerSubmitData b) {
        BeerSubmitData.BeerUnit beerUnit = null;
        for (BeerSubmitData.BeerUnit unit :
                b.getListUnit()) {
            if (unit.getBeer_unit_second_id().equals(this.getBeer_unit())) {
                beerUnit = unit;
            }
        }
        b.setListUnit(new BeerSubmitData.BeerUnit[]{ beerUnit });
        this.beerSubmitData = b;
        return this;
    }
}
