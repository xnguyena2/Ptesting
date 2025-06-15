package com.example.heroku.model.statistics;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class BenifitByProduct {
    private String product_name;
    private String product_unit_name;
    private String p_product_name;
    private String p_product_unit_name;
    private String p_product_group_unit_name;
    private String product_second_id;
    private String product_unit_second_id;
    private float revenue;
    private float profit;
    private float number_unit;
    protected LocalDateTime createat;
}
