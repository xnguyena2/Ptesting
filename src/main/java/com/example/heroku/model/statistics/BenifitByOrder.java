package com.example.heroku.model.statistics;

import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
public class BenifitByOrder {
    private Timestamp createat;
    private String package_second_id;
    private float revenue;
    private float profit;
    private float price;
    private float ship_price;
}
