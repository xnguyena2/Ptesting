package com.example.heroku.request.Order;

import com.example.heroku.model.ProductUnitOrder;
import com.example.heroku.model.PackageOrder;
import com.example.heroku.services.VietNamAddress;

import java.util.ArrayList;

public class OrderSearchResult  extends order.OrderSearchResult {

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

    public static class PackageOrderData extends order.OrderSearchResult.PackageOrderData {

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
            setReal_price(source.getReal_price());
            setShip_price(source.getShip_price());
            setStatus(source.getStatus());
            setCreateat(source.getCreateat());
        }

        public BeerOrderData Add(entity.BeerOrder newItem) {
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

        public static class BeerOrderData extends order.OrderSearchResult.PackageOrderData.BeerOrderData {

            public BeerOrderData(entity.BeerOrder source) {

                setPackage_order_second_id(source.getPackage_order_second_id());
                setBeer_second_id(source.getBeer_second_id());
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
    }
}
