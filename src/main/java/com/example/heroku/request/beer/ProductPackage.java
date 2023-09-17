package com.example.heroku.request.beer;

import com.example.heroku.model.UserPackage;
import com.example.heroku.model.UserPackageDetail;
import com.example.heroku.util.Util;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@SuperBuilder()
@NoArgsConstructor
@AllArgsConstructor
public class ProductPackage extends UserPackageDetail {

    private ProductUnit[] product_units;

    public UserPackageDetail getUserPackageDetail() {
        return clone(this);
    }

    public ProductPackage AutoFill() {
        return (ProductPackage) super.AutoFill();
    }

    public boolean isEmpty() {
        return product_units == null;
    }

    public UserPackage[] getUserPackage() {
        if(product_units == null)
            return null;
        UserPackage[] listResult = new UserPackage[product_units.length];
        for (int i = 0; i < product_units.length; i++) {
            ProductUnit beer = product_units[i];
            listResult[i] = UserPackage.builder()
                    .package_second_id(this.getPackage_second_id())
                    .product_second_id(beer.getProduct_id())
                    .product_unit_second_id(beer.product_unit_id)
                    .device_id(this.getDevice_id())
                    .number_unit(beer.number_unit)
                    .group_id(this.group_id)
                    .createat(Util.getInstance().Now())
                    .build();
        }
        return listResult;
    }


    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductUnit {
        private String product_id;
        private String product_unit_id;
        private int number_unit;
    }
}
