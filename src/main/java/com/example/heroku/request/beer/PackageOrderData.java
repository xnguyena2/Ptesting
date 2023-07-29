package com.example.heroku.request.beer;

import com.example.heroku.model.Product;
import com.example.heroku.model.ProductOrder;
import com.example.heroku.model.ProductUnitOrder;
import com.example.heroku.model.PackageOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackageOrderData {

    private boolean preOrder;

    private PackageOrder packageOrder;

    private BeerOrderData[] beerOrders;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BeerOrderData {
        private ProductOrder productOrder;

        private ProductUnitOrder[] productUnitOrders;

        public BeerOrderData UpdateName(Product product) {
            if (productOrder != null) {
                productOrder.setGroup_id(product.getGroup_id());
                productOrder.setName(product.getName());

                for (ProductUnitOrder unit :
                        productUnitOrders) {
                    unit.setGroup_id(product.getGroup_id());
                }
            }
            return this;
        }
    }
}
