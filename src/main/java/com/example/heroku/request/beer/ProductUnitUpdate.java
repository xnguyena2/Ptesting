package com.example.heroku.request.beer;

import com.example.heroku.model.ProductUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductUnitUpdate {
    private String group_id;
    private String product_second_id;
    private String product_unit_second_id;

    private int inventory_number;
    private boolean enable_warehouse;
    private ProductUnit.Status status;
}
