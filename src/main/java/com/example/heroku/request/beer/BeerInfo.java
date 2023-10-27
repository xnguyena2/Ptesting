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
        product.UpdateMetaSearch().AutoFill();
        final String productID = product.getProduct_second_id();
        for (ProductUnit unit :
                productUnit) {
            unit.setGroup_id(this.getProduct().getGroup_id());
            unit.setProduct_second_id(productID);
            unit.AutoFill();
        }
        return this;
    }
}
