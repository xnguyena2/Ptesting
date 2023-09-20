package com.example.heroku.request.beer;

import com.example.heroku.model.Buyer;
import com.example.heroku.model.UserPackage;
import com.example.heroku.model.UserPackageDetail;
import com.example.heroku.util.Util;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder()
@NoArgsConstructor
@AllArgsConstructor
public class ProductPackage extends UserPackageDetail {

    private Buyer buyer;

    private UserPackage[] product_units;

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
        for (UserPackage userPackage :
                product_units) {
            userPackage.setPackage_second_id(this.getPackage_second_id());
            userPackage.setGroup_id(this.getGroup_id());
            userPackage.setDevice_id(this.getDevice_id());
            userPackage.AutoFill();
        }
        return product_units;
    }
}
