package com.example.heroku.request.order;

import com.example.heroku.model.PackageOrder;
import com.example.heroku.model.ProductOrder;
import com.example.heroku.model.ProductUnitOrder;
import com.example.heroku.model.count.ResultWithCount;
import com.example.heroku.services.VietNamAddress;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchResult extends ResultWithCount {
    protected List<PackageOrderData> result;

    public OrderSearchResult Add(PackageOrderData newItem) {
        if (result == null) {
            result = new ArrayList<>();
        }
        result.add(newItem);
        return this;
    }

    public PackageOrderData[] GetResultAsArray() {
        return result.toArray(new PackageOrderData[0]);
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PackageOrderData extends PackageOrder{

        protected String region;
        protected String district;
        protected String ward;

        protected List<BeerOrderData> beerOrderList;

        @EqualsAndHashCode(callSuper = true)
        @Data
        @SuperBuilder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class BeerOrderData extends ProductOrder {
            protected List<ProductUnitOrder> beerUnitOrderList;

            public BeerOrderData(ProductOrder source) {

                setPackage_order_second_id(source.getPackage_order_second_id());
                setProduct_second_id(source.getProduct_second_id());
                setVoucher_second_id(source.getVoucher_second_id());
                setTotal_price(source.getTotal_price());
                setShip_price(source.getShip_price());
                setName(source.getName());

            }

            public BeerOrderData Add(ProductUnitOrder newItem) {
                if (beerUnitOrderList == null) {
                    beerUnitOrderList = new ArrayList<>();
                }
                beerUnitOrderList.add(newItem);
                return this;
            }
        }

        public PackageOrderData(PackageOrder source) {

            setPackage_order_second_id(source.getPackage_order_second_id());
            setUser_device_id(source.getUser_device_id());
            setReciver_address(source.getReciver_address());
            try {
                setRegion(VietNamAddress.getInstance().GetRegionName(source.getRegion_id()));
                setDistrict(VietNamAddress.getInstance().GetDistrictName(source.getRegion_id(), source.getDistrict_id()));
                setWard(VietNamAddress.getInstance().GetWardName(source.getRegion_id(), source.getDistrict_id(), source.getWard_id()));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            setReciver_fullname(source.getReciver_fullname());
            setPhone_number(source.getPhone_number());
            setPhone_number_clean(source.getPhone_number_clean());
            setTotal_price(source.getTotal_price());
            setReal_price(source.getReal_price());
            setShip_price(source.getShip_price());
            setDiscount(source.getDiscount());
            setStatus(source.getStatus());
            setCreateat(source.getCreateat());
        }

        public BeerOrderData Add(ProductOrder newItem) {
            if (beerOrderList == null) {
                beerOrderList = new ArrayList<>();
            }
            BeerOrderData newI = new BeerOrderData(newItem);
            beerOrderList.add(newI);
            return newI;
        }

        public BeerOrderData[] GetResultAsArray() {
            return beerOrderList.toArray(new BeerOrderData[0]);
        }
    }
}
