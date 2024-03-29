package com.example.heroku.model;

import com.example.heroku.model.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="product_unit_order")
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductUnitOrder extends BaseEntity {

    protected String name;

    protected String package_order_second_id;

    protected String product_second_id;

    protected String product_unit_second_id;

    protected int number_unit;

    protected float price;

    protected float total_discount;

    public ProductUnitOrder AutoFill(String packageOrderSecondID) {
        super.AutoFill();
        this.package_order_second_id = packageOrderSecondID;
        return this;
    }

    public ProductUnit UpdateName(ProductUnit productUnit){
        this.name = productUnit.getName();
        return productUnit;
    }

}
