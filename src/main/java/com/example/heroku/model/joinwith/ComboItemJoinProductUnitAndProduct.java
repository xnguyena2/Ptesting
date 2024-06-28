package com.example.heroku.model.joinwith;

import com.example.heroku.model.ProductUnit;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComboItemJoinProductUnitAndProduct extends ProductUnit {

    protected String product_name;

    private float unit_number;


}
