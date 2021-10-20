package com.example.heroku.response;

import com.example.heroku.services.VietNamAddress;
import lombok.Data;

@Data
public class BuyerData {
    private String reciver_fullname;

    private String phone_number_clean;

    private String phone_number;

    private String reciver_address;

    private String region;

    private String district;

    private String ward;

    private float total_price;

    public BuyerData(com.example.heroku.model.Buyer b){
        setPhone_number_clean(b.getPhone_number_clean());
        setPhone_number(b.getPhone_number());
        setReciver_address(b.getReciver_address());
        setReciver_fullname(b.getReciver_fullname());
        setTotal_price(b.getTotal_price());
        try {
            setRegion(VietNamAddress.getInstance().GetRegionName(b.getRegion_id()));
            setDistrict(VietNamAddress.getInstance().GetDistrictName(b.getRegion_id(), b.getDistrict_id()));
            setWard(VietNamAddress.getInstance().GetWardName(b.getRegion_id(), b.getDistrict_id(), b.getWard_id()));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
