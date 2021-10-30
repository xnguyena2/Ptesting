package com.example.heroku.response;

import com.example.heroku.model.BeerOrder;
import com.example.heroku.model.PackageOrder;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
@Data
public class BeerOrderStatus extends BeerOrder {

    private PackageOrder.Status status;
}
