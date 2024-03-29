package com.example.heroku.model.statistics;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BenifitByMonth {
    private int count;
    private double revenue;
    private double profit;
    private double cost;
    private double price;
    private double ship_price;
    private double discount;
    private int buyer;
}