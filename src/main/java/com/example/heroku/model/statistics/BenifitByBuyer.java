package com.example.heroku.model.statistics;

import lombok.Data;

@Data
public class BenifitByBuyer {
    private String id;
    private String name;
    private String phone;
    private float revenue;
    private float profit;
    private int count;
}
