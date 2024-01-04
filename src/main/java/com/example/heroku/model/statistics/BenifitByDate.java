package com.example.heroku.model.statistics;

import lombok.Data;

import java.sql.Date;

@Data
public class BenifitByDate {
    private Date local_time;
    private float revenue;
    private float profit;
    private float cost;
    private int count;
}
