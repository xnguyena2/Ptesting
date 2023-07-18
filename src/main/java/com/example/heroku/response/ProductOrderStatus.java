package com.example.heroku.response;

import com.example.heroku.model.ProductOrder;
import com.example.heroku.model.PackageOrder;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper=true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrderStatus extends ProductOrder {

    private PackageOrder.Status status;
}
