package com.example.heroku.request.datetime;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Calendar;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NgbDateStruct {
    private int day;
    private int month;
    private int year;

    public Timestamp ToDateTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month-1, day);
        return new Timestamp(cal.getTime().getTime());
    }
    public static NgbDateStruct FromTimestamp(Timestamp time) {
        if (time == null)
            return null;
        long timestamp = time.getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return NgbDateStruct.builder()
                .year(cal.get(Calendar.YEAR))
                .month(cal.get(Calendar.MONTH)+1)
                .day(cal.get(Calendar.DAY_OF_MONTH))
                .build();
    }
}
