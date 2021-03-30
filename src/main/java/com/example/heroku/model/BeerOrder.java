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
@Table(name="beer_order")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeerOrder {
    @Id
    String id;

    private String name;

    private String package_order_second_id;

    private String beer_second_id;

    private String voucher_second_id;

    private float total_price;

    private float ship_price;

    private Timestamp createat;

    public BeerOrder AutoFill(String packageOrderSecondID){
        this.package_order_second_id = packageOrderSecondID;
        this.createat = new Timestamp(new Date().getTime());
        return this;
    }

}
