package com.example.heroku.model.statistics;

import lombok.Data;

@Data
public class BenifitOfOrder {
    private float ship_price;
    private float discount;
    private float price;
    private float revenue;
    private float profit;
    private float cost;
    private int count;
    private int buyer;
    private float discount_promotional;
    private float discount_by_point;
    private float return_price;
    private float additional_fee;
}
