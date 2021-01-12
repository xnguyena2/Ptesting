package com.example.heroku.request.datetime;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Calendar;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NgbDateStruct {
    private int day;
    private int month;
    private int year;

    public Timestamp ToDateTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        return new Timestamp(cal.getTime().getTime());
    }
}
