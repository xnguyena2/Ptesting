package com.example.heroku.model.productprice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPriceChange {

    private String product_second_id;

    private String product_unit_second_id;

    private ProductPriceItem[] price_change;

    private String product_name;

    private String product_unit_name;
}
