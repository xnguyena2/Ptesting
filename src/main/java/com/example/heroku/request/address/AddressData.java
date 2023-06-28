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

    private VietNamAddress.AddressItemData region;

    private VietNamAddress.AddressItemData district;

    private VietNamAddress.AddressItemData ward;

    private String region_text_format;

    public UserAddress CovertModel(boolean isResetID) {
        return UserAddress.builder()
                .address_id(isResetID?Util.getInstance().GenerateID():address_id)
                .device_id(device_id)
                .reciver_fullname(reciver_fullname)
                .phone_number(phone_number)
                .house_number(house_number)
                .region(region.id)
                .district(district.id)
                .ward(ward.id)
                .build();
    }

    public static Mono<AddressData> FromModel(UserAddress src) {
        String region_text_format = "";

        VietNamAddress.AddressItemData region = VietNamAddress.AddressItemData.builder()
                .id(-1)
                .name("undefiend")
                .build();

        VietNamAddress.AddressItemData district= VietNamAddress.AddressItemData.builder()
                .id(-1)
                .name("undefiend")
                .build();

        VietNamAddress.AddressItemData ward= VietNamAddress.AddressItemData.builder()
                .id(-1)
                .name("undefiend")
                .build();
        try {
            region.setId(src.getRegion());
            region.setName(VietNamAddress.getInstance().GetRegionName(src.getRegion()));

            district.setId(src.getDistrict());
            district.setName(VietNamAddress.getInstance().GetDistrictName(src.getRegion(), src.getDistrict()));

            ward.setId(src.getWard());
            ward.setName(VietNamAddress.getInstance().GetWardName(src.getRegion(), src.getDistrict(), src.getWard()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        region_text_format = ward.getName() + ", "
                + district.getName() + ", "
                + region.getName();

        return Mono.just(AddressData.builder()
                .address_id(src.getAddress_id())
                .device_id(src.getDevice_id())
                .reciver_fullname(src.getReciver_fullname())
                .phone_number(src.getPhone_number())
                .house_number(src.getHouse_number())
                .region(region)
                .district(district)
                .ward(ward)
                .region_text_format(region_text_format)
                .build());
    }
}
