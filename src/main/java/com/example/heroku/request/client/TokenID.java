package com.example.heroku.request.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TokenID {
    private String group_id;
    private String id;
    private long time_life_mili_secs;
}
