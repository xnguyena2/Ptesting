package com.example.heroku.request.beer;

import com.example.heroku.model.Product;
import com.example.heroku.model.ProductUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeerInfo {
    private Product product;

    private ProductUnit[] productUnit;

    public BeerInfo SetBeerUnit(List<ProductUnit> productUnitList) {
        productUnit = new ProductUnit[productUnitList.size()];
        productUnitList.toArray(productUnit);
        return this;
    }

    public BeerInfo CorrectData() {
        for (ProductUnit unit :
                productUnit) {
            if (unit.getGroup_id() == null) {
                unit.setGroup_id(this.getProduct().getGroup_id());
            }
        }
        return this;
    }
}
