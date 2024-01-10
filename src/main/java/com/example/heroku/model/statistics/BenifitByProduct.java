package com.example.heroku.model.statistics;

import lombok.Data;

import java.sql.Date;

@Data
public class BenifitByProduct {
    private String product_second_id;
    private String product_unit_second_id;
    private float revenue;
    private int number_unit;
}
