package com.example.heroku.services;

import com.example.heroku.model.repository.ShippingProviderRepository;
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

    public Mono<com.example.heroku.model.ShippingProvider> CreateShipProvider(String id, String config) throws Exception {
        if(!id.equals(GHN.ID))
            throw new Exception("Not match ID");
        GHN ghn = new ObjectMapper().readValue(config, GHN.class);
        return shippingProviderRepository
                .deleteByProviderId(id)
                .then(Mono.just(ghn)
                        .flatMap(provider -> shippingProviderRepository.save(
                                com.example.heroku.model.ShippingProvider.builder()
                                        .provider_id(id)
                                        .name(GHN.NAME)
                                        .config(config)
                                        .build()
                        )));
    }

    public Mono<com.example.heroku.model.ShippingProvider> GetShipProvider(String id){
        return shippingProviderRepository.findByProviderId(id);
    }
    public Mono<GHN> GetGHNShippingProvider() {
        return shippingProviderRepository.findByProviderId(GHN.ID)
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

    public Mono<Price> GetShippingPrice(String providerID, float packageWeight, float packageVolumetric, int region, int district) {
        return shippingProviderRepository.findByProviderId(providerID)
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

        public static String NAME = "Giao H??ng Nhanh";

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
        if (region == 294) {//h??? ch?? minh
            if (Arrays.asList(new int[]{
                    507,//nh?? b??
                    503,//b??nh ch??nh
                    506,//h??c m??n
                    505//c??? chi
            }).contains(district)) {
                return DistanceType.OUTSIDE_REGION_TYPE2;
            } else if (Arrays.asList(new int[]{
                    492,//q9
                    495,//q12
                    502,//th??? ?????c
                    496//b??nh t??n
            }).contains(district)) {
                return DistanceType.OUTSIDE_REGION_TYPE1;
            }
            return DistanceType.INSIDE_REGION;
        } else if (Arrays.asList(new int[]{
                316,//b??nh ?????nh
                293,//gia lai
                308,//ph?? y??n
                292,//?????k L???k,
                321,//kh??nh h??a,
                328,//ninh thu???n
                306,//l??m ?????ng
                340,//?????k n??ng
                287,//b??nh thu???n,
                286,//b??nh ph?????c
                317,//?????ng nai
                280,//b?? r???a - v??ng t??u
                332,//t??y ninh
                285,//b??nh d????ng,
                325,//long an
                318,//?????ng th??p
                336,//ti???n giang
                284,//b???n tre
                314,//tr?? vinh
                337,//v??nh long
                319,//h???u giang
                290,//c???n th??
                278,//an giang
                322,//ki??n giang
                289,//c?? mau
                279,//b???c li??u
                313//s??c tr??ng
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
