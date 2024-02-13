package com.example.heroku.model.statistics;

import lombok.Data;

import java.sql.Date;

@Data
public class CountOrderByDate {
    private int count_return;
    private float revenue_return;

    private int count_cancel;
    private float revenue_cancel;

}
