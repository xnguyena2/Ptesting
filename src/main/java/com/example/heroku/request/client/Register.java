package com.example.heroku.request.client;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Register {
    private String group_id;
    private String id;
    private String firstName;
    private String lastName;
}
