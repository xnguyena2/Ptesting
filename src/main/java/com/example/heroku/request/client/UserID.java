package com.example.heroku.request.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserID {
    private String group_id;
    private String id;
    private int page;
    private int size;
}
