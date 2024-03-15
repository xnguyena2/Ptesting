package com.example.heroku.model.statistics;

import lombok.Data;

@Data
public class BenifitByPaymentTransaction {
    private String category;
    private float revenue;
    private float cost;
}
