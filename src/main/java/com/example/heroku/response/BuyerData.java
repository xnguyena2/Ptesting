package com.example.heroku.response;

import com.example.heroku.model.Buyer;
import com.example.heroku.services.VietNamAddress;
import lombok.Data;

@Data
public class BuyerData {

    private String group_id;

    private String device_id;

    private String reciver_fullname;

    private String phone_number_clean;

    private String phone_number;

    private String reciver_address;

    private String region;

    private String district;

    private String ward;

    private float total_price;

    private float points_discount;

    public BuyerData(com.example.heroku.model.Buyer b) {
        setDevice_id(b.getDevice_id());
        setGroup_id(b.getGroup_id());
        setPhone_number_clean(b.getPhone_number_clean());
        setPhone_number(b.getPhone_number());
        setReciver_address(b.getReciver_address());
        setReciver_fullname(b.getReciver_fullname());
        setTotal_price(b.getReal_price());
        setPoints_discount(b.getPoints_discount());
        try {
            setRegion(VietNamAddress.getInstance().GetRegionName(b.getRegion_id()));
            setDistrict(VietNamAddress.getInstance().GetDistrictName(b.getRegion_id(), b.getDistrict_id()));
            setWard(VietNamAddress.getInstance().GetWardName(b.getRegion_id(), b.getDistrict_id(), b.getWard_id()));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
