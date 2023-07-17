package com.example.heroku.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name="product_order")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrder extends entity.BeerOrder {
    @Id
    String id;

    public ProductOrder AutoFill(String packageOrderSecondID) {
        this.package_order_second_id = packageOrderSecondID;
        this.createat = new Timestamp(new Date().getTime());
        return this;
    }

}
