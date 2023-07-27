package com.example.heroku.services;

import com.example.heroku.model.repository.ShippingProviderRepository;
import com.example.heroku.request.ship.ShippingProviderData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Component
public class ShippingProvider {
    @Autowired
    ShippingProviderRepository shippingProviderRepository;

    public Mono<com.example.heroku.model.ShippingProvider> CreateShipProvider(ShippingProviderData config) {
        String groupID = config.getGroup_id();
        String id = config.getId();
        String configtxt = config.getJson();
        if (!id.equals(GHN.ID))
            return Mono.error(new Exception("Not GHN"));
        try {
            GHN ghn = new ObjectMapper().readValue(configtxt, GHN.class);
            return shippingProviderRepository
                    .deleteByProviderId(groupID, id)
                    .then(Mono.just(ghn)
                            .flatMap(provider -> shippingProviderRepository.save(
                                            com.example.heroku.model.ShippingProvider.builder()
                                                    .provider_id(id)
                                                    .name(GHN.NAME)
                                                    .config(configtxt)
                                                    .group_id(groupID)
                                                    .build()
                                    )
                            )
                    );
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }

    public Mono<com.example.heroku.model.ShippingProvider> GetShipProvider(String groupid, String id){
        return shippingProviderRepository.findByProviderId(groupid, id);
    }
    public Mono<GHN> GetGHNShippingProvider(String groupid) {
        return shippingProviderRepository.findByProviderId(groupid, GHN.ID)
                .map(shippingProvider -> {
                            try {
                                return new ObjectMapper().readValue(shippingProvider.getConfig(), GHN.class);
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                                return null;
                            }
                        }
                );
    }

    public Mono<Price> GetShippingPrice(String groupid, String providerID, float packageWeight, float packageVolumetric, int region, int district) {
        return shippingProviderRepository.findByProviderId(groupid, providerID)
                .map(shippingProvider -> {
                            try {
                                GHN ghn = new ObjectMapper().readValue(shippingProvider.getConfig(), GHN.class);
                                return Price.builder().price(ghn.CalculateShipPrice(packageWeight, packageVolumetric, region, district)).build();
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                                return null;
                            }
                        }
                );
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Price{
        private float price;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GHN {

        public static String ID = "GHN";

        public static String NAME = "Giao Hàng Nhanh";

        private float weigitExchange;

        private PackagePrice listPackagePriceDetail[];


        public float CalculateShipPrice(float packageWeight, float packageVolumetric, int region, int district) {
            //get product in store
            if(region == 0)
                return 0;
            float shipPrice = 0;
            float weightExchange = this.getWeightExchange(packageVolumetric);
            float weight = Math.max(packageWeight, weightExchange);
            DistanceType distanceType = GetDistancetype(region, district);

            for (PackagePrice packageDetail :
                    this.listPackagePriceDetail) {
                if (packageDetail.reciverLocation == distanceType) {
                    shipPrice = packageDetail.priceMaxWeight;
                    if (weight > packageDetail.maxWeight) {
                        int extractSize = (int) Math.ceil((weight - packageDetail.maxWeight) / packageDetail.nextWeight);
                        shipPrice += extractSize * packageDetail.priceNextWeight;
                    }
                }
            }
            return shipPrice;
        }

        float getWeightExchange(float packageVolumetric){return packageVolumetric*weigitExchange;}

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class PackagePrice{
            private DistanceType reciverLocation;
            private float maxWeight;
            private float priceMaxWeight;

            private float nextWeight;
            private float priceNextWeight;
        }

    }

    public static DistanceType GetDistancetype(int region, int district) {
        if (region == 294) {//hồ chí minh
            if (Arrays.asList(new int[]{
                    507,//nhà bè
                    503,//bình chánh
                    506,//hóc môn
                    505//củ chi
            }).contains(district)) {
                return DistanceType.OUTSIDE_REGION_TYPE2;
            } else if (Arrays.asList(new int[]{
                    492,//q9
                    495,//q12
                    502,//thủ đức
                    496//bình tân
            }).contains(district)) {
                return DistanceType.OUTSIDE_REGION_TYPE1;
            }
            return DistanceType.INSIDE_REGION;
        } else if (Arrays.asList(new int[]{
                316,//bình định
                293,//gia lai
                308,//phú yên
                292,//Đắk Lắk,
                321,//khánh hòa,
                328,//ninh thuận
                306,//lâm đồng
                340,//đắk nông
                287,//bình thuận,
                286,//bình phước
                317,//đồng nai
                280,//bà rịa - vũng tàu
                332,//tây ninh
                285,//bình dương,
                325,//long an
                318,//đồng tháp
                336,//tiền giang
                284,//bến tre
                314,//trà vinh
                337,//vĩnh long
                319,//hậu giang
                290,//cần thơ
                278,//an giang
                322,//kiên giang
                289,//cà mau
                279,//bạc liêu
                313//sóc trăng
        }).contains(region)) {
            return DistanceType.INSIDE_GREGION;
        }
        return DistanceType.DIFFIRENT_GPREGION;
    }

    public enum DistanceType{
        INSIDE_REGION,
        OUTSIDE_REGION_TYPE1,
        OUTSIDE_REGION_TYPE2,
        INSIDE_GREGION,
        DIFFIRENT_GPREGION;
    }
}
