package com.example.heroku.request.client;

import com.example.heroku.request.client.base.BaseID;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserID extends BaseID {
    private String id;
    private long after_id;
}
