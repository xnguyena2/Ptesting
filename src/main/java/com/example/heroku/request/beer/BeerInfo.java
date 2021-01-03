package com.example.heroku.request.beer;

import com.example.heroku.model.Beer;
import com.example.heroku.model.BeerUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeerInfo {
    private Beer beer;

    private BeerUnit[] beerUnit;

    public BeerInfo SetBeerUnit(List<BeerUnit> beerUnitList){
        beerUnit = new BeerUnit[beerUnitList.size()];
        beerUnitList.toArray(beerUnit);
        return this;
    }
}
