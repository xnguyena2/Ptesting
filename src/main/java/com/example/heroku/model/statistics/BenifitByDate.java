package com.example.heroku.model.statistics;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class BenifitByDate extends BenifitOfOrder{
    private Date local_time;
}
