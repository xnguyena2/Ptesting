package com.example.heroku.request.datetime;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NgbDateStruct {
    private int day;
    private int month;
    private int year;

    // Convert to LocalDateTime (00:00:00 time)
    public LocalDateTime toLocalDateTime() {
        return LocalDateTime.of(year, month, day, 0, 0, 0);
    }

    // Create NgbDateStruct from LocalDateTime
    public static NgbDateStruct fromLocalDateTime(LocalDateTime dateTime) {
        if (dateTime == null)
            return null;
        return NgbDateStruct.builder()
                .year(dateTime.getYear())
                .month(dateTime.getMonthValue())
                .day(dateTime.getDayOfMonth())
                .build();
    }

}
