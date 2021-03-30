package com.example.heroku.request.Order;

import com.example.heroku.model.PackageOrder;
import com.example.heroku.model.count.ResultWithCount;
import com.example.heroku.services.VietNamAddress;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchResult  extends ResultWithCount {
    private List<PackageOrderData> result;

    public OrderSearchResult Add(PackageOrder newItem) {
        if (result == null) {
            result = new ArrayList<>();
        }
        result.add(new PackageOrderData(newItem));
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
    }
}
