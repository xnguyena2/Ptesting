package com.example.heroku.services;

import com.example.heroku.util.Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class VietNamAddress {

    private static VietNamAddress instance;

    private AddressFromat allAddress;

    private VietNamAddress(){
    }

    synchronized public static VietNamAddress getInstance() {
        if (instance == null)
        {
            instance = new VietNamAddress();
        }
        return instance;
    }

    public void SaveAllToDatabase(boolean forTest) throws IOException {
        String json;//= new String(Files.readAllBytes(Paths.get("Asset/AllAddressInfo.json")));

        StringBuilder jsonBuilder = new StringBuilder();

        Path path;
        if (forTest) {
            path = Paths.get("Asset/AllAddressInfoTest.json");
        } else {
            path = Paths.get("Asset/AllAddressInfo.json");
        }

        // Java 8, default UTF-8
        try (BufferedReader reader = Files.newBufferedReader(path)) {

            String str;
            while ((str = reader.readLine()) != null) {
                jsonBuilder.append(str);
                jsonBuilder.append(System.lineSeparator());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        json = jsonBuilder.toString();

        allAddress = new ObjectMapper().readValue(json, AddressFromat.class);

        int totalAddress = 0;
        for (AddressFromat.Region region :
                allAddress.regions) {
            for (AddressFromat.Region.DistrictContent.District district :
                    region.districts.data) {
                totalAddress += district.wards.data.length;
            }
        }

        System.out.println("Total rows: " + totalAddress);
    }

    public String GetRegionName(int regionID) throws IOException {
        if (allAddress == null) {
            this.SaveAllToDatabase(false);
        }
        for (VietNamAddress.AddressFromat.Region region:
                allAddress.getRegions()) {
            if(region.id == regionID)
                return region.name;
        }
        return null;
    }

    public String GetDistrictName(int regionID, int districtID) throws IOException {
        if (allAddress == null) {
            this.SaveAllToDatabase(false);
        }
        for (VietNamAddress.AddressFromat.Region region:
                allAddress.getRegions()) {
            if(region.id == regionID) {
                for (AddressFromat.Region.DistrictContent.District district :
                        region.districts.data) {
                    if (district.id == districtID)
                        return district.name;
                }
            }
        }
        return null;
    }

    public Flux<VietNamAddress.AddressFromat.Region> GetAllRegion() throws IOException {
        if (allAddress == null) {
            this.SaveAllToDatabase(false);
        }
        return Flux.just(allAddress.regions)
                .flatMap(region ->
                        Mono.just(region));
    }

    public Flux<VietNamAddress.AddressFromat.Region.DistrictContent.District> GetAllDistrict(int ofRegion) throws IOException {
        if (allAddress == null) {
            this.SaveAllToDatabase(false);
        }
        return Flux.just(allAddress.regions)
                .filter(region -> region.id == ofRegion)
                .flatMap(region ->
                        Flux.just(region.districts.data)
                        .flatMap(district -> Mono.just(district))
                );
    }

    public Flux<VietNamAddress.AddressFromat.Region.DistrictContent.District.WardContent.Ward> GetAllWard(int ofRegion, int andDistrict) throws IOException {
        if (allAddress == null) {
            this.SaveAllToDatabase(false);
        }
        return Flux.just(allAddress.regions)
                .filter(region -> region.id == ofRegion)
                .flatMap(region ->
                        Flux.just(region.districts.data)
                                .filter(district -> district.id == andDistrict)
                                .flatMap(district -> Flux.just(district.wards.data))
                                .flatMap(ward -> Mono.just(ward))
                );
    }


    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressFromat{
        private Region[] regions;
        private Object[] districts;
        private Object[] wards;

        @Builder
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Region{
            private String name;
            private int id;

            private DistrictContent districts;

            @Builder
            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            public static class DistrictContent {

                private District[] data;

                @Builder
                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                public static class District {

                    private String name;
                    private int id;
                    private WardContent wards;


                    @Builder
                    @Data
                    @NoArgsConstructor
                    @AllArgsConstructor
                    public static class WardContent {

                        private Ward[] data;

                        @Builder
                        @Data
                        @NoArgsConstructor
                        @AllArgsConstructor
                        public static class Ward {

                            private String name;
                            private int id;
                        }
                    }
                }
            }
        }
    }
}
