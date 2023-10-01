package com.example.heroku.request.client;

import com.example.heroku.request.client.base.BaseID;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Register extends BaseID {
    private String id;
    private String firstName;
    private String lastName;
}
