package com.example.heroku.model.statistics;

import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class BenifitByDateHour extends BenifitOfOrder{
    private LocalDateTime local_time;
}
