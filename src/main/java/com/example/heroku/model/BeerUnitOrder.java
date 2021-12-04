package com.example.heroku.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name="beer_unit_order")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BeerUnitOrder extends entity.BeerUnitOrder{
    @Id
    String id;

    public BeerUnitOrder AutoFill(String packageOrderSecondID) {
        this.package_order_second_id = packageOrderSecondID;
        this.createat = new Timestamp(new Date().getTime());
        return this;
    }

    public BeerUnit UpdateName(BeerUnit beerUnit){
        this.name = beerUnit.getName();
        return beerUnit;
    }

}
