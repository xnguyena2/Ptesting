package com.example.heroku.model.statistics;

import lombok.Data;

@Data
public class BenifitByUser {
    private String id;
    private String name;
    private float revenue;
    private int count;
}
