package com.example.heroku.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name="beer_unit_order")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeerUnitOrder {
    @Id
    String id;

    private String package_order_second_id;

    private String beer_second_id;

    private String beer_unit_second_id;

    private int number_unit;

    private float price;

    private float total_discount;

    private Timestamp createat;

    public BeerUnitOrder AutoFill(String packageOrderSecondID) {
        this.package_order_second_id = packageOrderSecondID;
        this.createat = new Timestamp(new Date().getTime());
        return this;
    }

}
