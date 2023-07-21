package com.example.heroku.model.count;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder(builderMethodName = "baseBuilder")
@NoArgsConstructor
@AllArgsConstructor
public class ResultWithCount {
    private float count;
}
