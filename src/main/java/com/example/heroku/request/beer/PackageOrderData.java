package com.example.heroku.request.beer;

import com.example.heroku.model.Beer;
import com.example.heroku.model.BeerUnitOrder;
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
        private com.example.heroku.model.BeerOrder beerOrder;

        private BeerUnitOrder[] beerUnitOrders;

        public BeerOrderData UpdateName(Beer beer) {
            if (beerOrder != null) {
                beerOrder.setName(beer.getName());
            }
            return this;
        }
    }
}
