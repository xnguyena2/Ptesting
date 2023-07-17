package com.example.heroku.request.beer;

import com.example.heroku.model.UserPackage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeerPackage {

    private String deviceID;

    private String beerID;

    private int voucherID;

    private int address_id;

    private float total_price;

    private float ship_price;

    private BeerUnit[] beerUnits;

    public UserPackage[] getUserPackage() {
        if(beerUnits == null)
            return null;
        UserPackage[] listResult = new UserPackage[beerUnits.length];
        for (int i = 0; i < beerUnits.length; i++) {
            BeerUnit beer = beerUnits[i];
            listResult[i] = UserPackage.builder()
                    .product_second_id(this.beerID)
                    .product_unit_second_id(beer.beerUnitID)
                    .device_id(this.deviceID)
                    .number_unit(beer.numberUnit)
                    .createat(new Timestamp(new Date().getTime()))
                    .build();
        }
        return listResult;
    }


    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BeerUnit{
        private String beerUnitID;
        private int numberUnit;
    }
}
