package com.example.heroku.request.address;

import com.example.heroku.model.UserAddress;
import com.example.heroku.services.VietNamAddress;
import com.example.heroku.util.Util;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressData {

    private String address_id;

    private String device_id;

    private String reciver_fullname;

    private String phone_number;

    private String house_number;

    private int region;

    private int district;

    private int ward;

    private String region_text_format;

    public UserAddress CovertModel() {
        return UserAddress.builder()
                .address_id(Util.getInstance().GenerateID())
                .device_id(device_id)
                .reciver_fullname(reciver_fullname)
                .phone_number(phone_number)
                .house_number(house_number)
                .region(region)
                .district(district)
                .ward(ward)
                .build();
    }

    public static Mono<AddressData> FromModel(UserAddress src) {
        String region_text_format = "";
        try {
            region_text_format = VietNamAddress.getInstance().GetWardName(src.getRegion(), src.getDistrict(), src.getWard()) + ", "
                    + VietNamAddress.getInstance().GetDistrictName(src.getRegion(), src.getDistrict()) + ", "
                    + VietNamAddress.getInstance().GetRegionName(src.getRegion());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Mono.just(AddressData.builder()
                .address_id(src.getAddress_id())
                .device_id(src.getDevice_id())
                .reciver_fullname(src.getReciver_fullname())
                .phone_number(src.getPhone_number())
                .house_number(src.getHouse_number())
                .region(src.getRegion())
                .district(src.getDistrict())
                .ward(src.getWard())
                .region_text_format(region_text_format)
                .build());
    }
}
