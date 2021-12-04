package com.example.heroku.response;

import com.example.heroku.model.BeerOrder;
import com.example.heroku.model.PackageOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper=true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BeerOrderStatus extends BeerOrder {

    private PackageOrder.Status status;
}
