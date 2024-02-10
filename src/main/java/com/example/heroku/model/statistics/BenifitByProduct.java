package com.example.heroku.model.statistics;

import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
public class BenifitByProduct {
    private String product_name;
    private String product_unit_name;
    private String product_second_id;
    private String product_unit_second_id;
    private float revenue;
    private float profit;
    private int number_unit;
    protected Timestamp createat;
}
