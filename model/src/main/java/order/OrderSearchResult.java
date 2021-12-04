package order;

import count.ResultWithCount;
import entity.BeerOrder;
import entity.BeerUnitOrder;
import entity.PackageOrder;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchResult extends ResultWithCount {
    protected List<PackageOrderData> result;

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PackageOrderData extends PackageOrder {

        protected String region;
        protected String district;
        protected String ward;

        protected List<BeerOrderData> beerOrderList;


        @EqualsAndHashCode(callSuper = true)
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class BeerOrderData extends BeerOrder {
            protected List<BeerUnitOrder> beerUnitOrderList;
        }
    }
}
