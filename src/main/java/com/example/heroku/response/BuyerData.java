package com.example.heroku.response;

import com.example.heroku.model.Buyer;
import com.example.heroku.services.VietNamAddress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyerData extends Buyer {


    private String region;

    private String district;

    private String ward;

    public BuyerData(com.example.heroku.model.Buyer b) {

        setGroup_id(b.getGroup_id());
        setCreateat(b.getCreateat());
        setDevice_id(b.getDevice_id());
        setReciver_fullname(b.getReciver_fullname());
        setPhone_number_clean(b.getPhone_number_clean());
        setPhone_number(b.getPhone_number());
        setReciver_address(b.getReciver_address());
        setRegion_id(b.getRegion_id());
        setDistrict_id(b.getDistrict_id());
        setWard_id(b.getWard_id());
        setReal_price(b.getReal_price());
        setTotal_price(b.getTotal_price());
        setShip_price(b.getShip_price());
        setPoint(b.getPoint());
        setDiscount(b.getDiscount());
        setMeta_search(b.getMeta_search());
        setStatus(b.getStatus());
        setDebt_in_come(b.getDebt_in_come());
        setDebt_out_come(b.getDebt_out_come());

        try {
            setRegion(VietNamAddress.getInstance().GetRegionName(b.getRegion_id()));
            setDistrict(VietNamAddress.getInstance().GetDistrictName(b.getRegion_id(), b.getDistrict_id()));
            setWard(VietNamAddress.getInstance().GetWardName(b.getRegion_id(), b.getDistrict_id(), b.getWard_id()));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
