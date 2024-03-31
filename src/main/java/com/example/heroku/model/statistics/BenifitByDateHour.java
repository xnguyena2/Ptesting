package com.example.heroku.model.statistics;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class BenifitByDateHour extends BenifitOfOrder{
    private LocalDateTime local_time;
}
