package com.example.heroku.model;

import com.example.heroku.util.Util;
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

    private String beer_unit_second_id;

    private String beer;

    private String name;

    private float price;

    private float discount;

    private Timestamp date_expire;

    private float volumetric;

    private float weight;

    private Timestamp createat;


    public BeerUnit AutoFill(){
        if(this.beer_unit_second_id==null || this.beer_unit_second_id == "")
            this.beer_unit_second_id = Util.getInstance().GenerateID();
        this.createat = new Timestamp(new Date().getTime());
        return this;
    }
}

