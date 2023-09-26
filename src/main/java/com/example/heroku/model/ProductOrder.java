package com.example.heroku.model;

import com.example.heroku.model.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="product_order")
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrder extends BaseEntity {

    protected String name;

    protected String package_order_second_id;

    protected String product_second_id;

    protected String voucher_second_id;

    protected float total_price;

    protected float ship_price;

    public ProductOrder AutoFill(String packageOrderSecondID) {
        super.AutoFill();
        this.package_order_second_id = packageOrderSecondID;
        return this;
    }

}
