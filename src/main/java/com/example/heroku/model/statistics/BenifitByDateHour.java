package com.example.heroku.model.statistics;

import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class BenifitByDateHour {
    private LocalDateTime local_time;
    private float revenue;
    private float profit;
    private float cost;
    private int count;
    private int buyer;

    private float ship_price;
    private float discount;
    private float price;
}
