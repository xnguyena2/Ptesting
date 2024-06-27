package com.example.heroku.request.beer;

import com.example.heroku.model.Product;
import com.example.heroku.model.ProductComboItem;
import com.example.heroku.model.ProductImport;
import com.example.heroku.model.ProductUnit;
import com.example.heroku.util.Util;
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

    private ProductComboItem[] listComboItem;

    public ProductUnit[] getProductUnitPrepareForInventory(String actionID) {
        if(actionID == null || actionID.isEmpty()){
            actionID = Util.getInstance().GenerateID();
        }
        for (ProductUnit unit :
                productUnit) {
            unit.setArg_action_id(actionID);
            unit.setArg_action_type(ProductImport.ImportType.UPDATE_NUMBER.getName());
        }
        return productUnit;
    }

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
