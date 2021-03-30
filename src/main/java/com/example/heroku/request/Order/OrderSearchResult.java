package com.example.heroku.request.Order;

import com.example.heroku.model.BeerOrder;
import com.example.heroku.model.BeerUnitOrder;
import com.example.heroku.model.PackageOrder;
import com.example.heroku.model.count.ResultWithCount;
import com.example.heroku.services.VietNamAddress;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchResult  extends ResultWithCount {
    private List<PackageOrderData> result;

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
    public static class PackageOrderData extends PackageOrder {

        private String region;
        private String district;
        private String ward;

        private List<BeerOrderData> beerOrderList;


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
            setTotal_price(source.getTotal_price());
            setShip_price(source.getShip_price());
            setStatus(source.getStatus());
            setCreateat(source.getCreateat());
        }

        public BeerOrderData Add(BeerOrder newItem) {
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

        @EqualsAndHashCode(callSuper = true)
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class BeerOrderData extends BeerOrder {
            private List<BeerUnitOrder> beerUnitOrderList;

            public BeerOrderData(BeerOrder source) {

                setPackage_order_second_id(source.getPackage_order_second_id());
                setBeer_second_id(source.getBeer_second_id());
                setVoucher_second_id(source.getVoucher_second_id());
                setTotal_price(source.getTotal_price());
                setShip_price(source.getShip_price());

            }

            public BeerOrderData Add(BeerUnitOrder newItem) {
                if (beerUnitOrderList == null) {
                    beerUnitOrderList = new ArrayList<>();
                }
                beerUnitOrderList.add(newItem);
                return this;
            }
        }
    }
}
