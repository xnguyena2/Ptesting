package com.example.heroku.model.statistics;

import lombok.Data;

import java.sql.Date;

@Data
public class BenifitByDate extends BenifitOfOrder{
    private Date local_time;
}
