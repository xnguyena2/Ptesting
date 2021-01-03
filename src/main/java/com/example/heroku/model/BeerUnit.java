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
@Table(name="beer_unit")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeerUnit {

    @Id
    String id;

    private String beer;

    private String name;

    private float price;

    private float discount;

    private Timestamp date_expire;

    private String ship_price;

    private Timestamp createat;


    public BeerUnit AutoFill(){
        this.createat = new Timestamp(new Date().getTime());
        return this;
    }
}

