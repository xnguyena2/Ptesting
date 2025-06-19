package com.example.heroku.model.statistics;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class BenifitByOrder {
    private LocalDateTime createat;
    private String package_second_id;
    private float revenue;
    private float profit;
    private float price;
    private float ship_price;
}
