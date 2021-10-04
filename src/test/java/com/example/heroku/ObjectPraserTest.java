package com.example.heroku;

import com.example.heroku.model.Beer;
import com.example.heroku.model.UserPackage;
import com.example.heroku.request.beer.BeerInfo;
import com.example.heroku.request.beer.BeerPackage;
import com.example.heroku.request.beer.BeerSubmitData;
import com.example.heroku.request.datetime.NgbDateStruct;
import com.example.heroku.services.ShippingProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class ObjectPraserTest {

    public void BeerSubmitDataTest() throws JsonProcessingException {
        String json = "{\"name\":\"Bia Trung Quốc\",\"beerSecondID\":\"1\",\"detail\":\"Vành đai 1 đoạn Hoàng Cầu - Voi Phục dài 2,27 km, tổng đầu tư hơn 7.200 tỷ đồng, do Ban Quản lý dự án đầu tư xây dựng công trình dân dụng và công nghiệp thành phố đang triển khai bằng nguồn vốn ngân sách TP Hà Nội.\\n\\nVành đai 2 trên cao, đoạn Vĩnh Tuy - Ngã Tư Vọng kết hợp mở rộng phần từ Vĩnh Tuy đến Ngã Tư Sở đang thực hiện theo hợp đồng BT (xây dựng - chuyển giao) ký kết giữa Hà Nội và Tập đoàn Vingroup.\",\"category\":\"squid\",\"listUnit\":[{\"beer\":\"1\",\"name\":\"Lon\",\"price\":17000,\"discount\":null,\"volumetric\":0.33,\"weight\":0.2,\"beer_unit_second_id\":null,\"dateExpir\":null}]}";

        BeerInfo beerInfo = new ObjectMapper().readValue(json, BeerSubmitData.class).GetBeerInfo();

        assertThat(beerInfo.getBeer().getBeer_second_id()).isEqualTo("1");
        assertThat(beerInfo.getBeer().getName()).isEqualTo("Bia Trung Quốc");
        assertThat(beerInfo.getBeer().getDetail()).isEqualTo("Vành đai 1 đoạn Hoàng Cầu - Voi Phục dài 2,27 km, tổng đầu tư hơn 7.200 tỷ đồng, do Ban Quản lý dự án đầu tư xây dựng công trình dân dụng và công nghiệp thành phố đang triển khai bằng nguồn vốn ngân sách TP Hà Nội.\n\nVành đai 2 trên cao, đoạn Vĩnh Tuy - Ngã Tư Vọng kết hợp mở rộng phần từ Vĩnh Tuy đến Ngã Tư Sở đang thực hiện theo hợp đồng BT (xây dựng - chuyển giao) ký kết giữa Hà Nội và Tập đoàn Vingroup.");
        assertThat(beerInfo.getBeer().getCategory()).isEqualTo(Beer.Category.SQUID);

        assertThat(beerInfo.getBeerUnit().length).isEqualTo(1);
        assertThat(beerInfo.getBeerUnit()[0].getName()).isEqualTo("Lon");
        assertThat(beerInfo.getBeerUnit()[0].getBeer()).isEqualTo("1");
        assertThat(beerInfo.getBeerUnit()[0].getDate_expire()).isEqualTo(null);
        assertThat(beerInfo.getBeerUnit()[0].getDiscount()).isEqualTo(0);
        assertThat(beerInfo.getBeerUnit()[0].getPrice()).isEqualTo(17000);
        assertThat(beerInfo.getBeerUnit()[0].getVolumetric()).isEqualTo(0.33f);
        assertThat(beerInfo.getBeerUnit()[0].getWeight()).isEqualTo(0.2f);

        json = "{\"beerSecondID\":\"1\",\"listUnit\":[{\"beer\":\"1\",\"name\":\"\",\"price\":0,\"discount\":0,\"volumetric\":0,\"weight\":0,\"beer_unit_second_id\":null,\"dateExpir\":null}]}";
        beerInfo = new ObjectMapper().readValue(json, BeerSubmitData.class).GetBeerInfo();

        assertThat(beerInfo.getBeerUnit().length).isEqualTo(1);
        assertThat(beerInfo.getBeerUnit()[0].getBeer()).isEqualTo("1");
        assertThat(beerInfo.getBeerUnit()[0].getDate_expire()).isEqualTo(null);
        assertThat(beerInfo.getBeerUnit()[0].getDiscount()).isEqualTo(0);
        assertThat(beerInfo.getBeerUnit()[0].getPrice()).isEqualTo(0);
        assertThat(beerInfo.getBeerUnit()[0].getWeight()).isEqualTo(0);
        assertThat(beerInfo.getBeerUnit()[0].getVolumetric()).isEqualTo(0);

        json = "{\"name\":\"Bia Nhập Lậu\",\"beerSecondID\":\"1\",\"detail\":\"Tỉnh lộ 769 từ nút giao Dầu Giây đến quốc lộ 51 sẽ được đầu tư hơn 1.600 tỷ đồng để mở rộng, đón đầu kết nối sân bay Long Thành.\\n\\nSở Giao thông vận tải Đồng Nai vừa trình UBND tỉnh Đồng Nai phê duyệt đầu tư dự án nâng cấp, mở rộng đường tỉnh 769 đi qua hai huyện Thống Nhất và Long Thành. Dự án là công trình giao thông chiến lược trong 5 năm tới nhằm kết nối các quốc lộ trọng yếu đi qua Đồng Nai như quốc lộ 1A, 20 và 51.\",\"category\":\"holothurian\",\"listUnit\":[{\"beer\":\"1\",\"name\":\"Lon\",\"price\":12000,\"discount\":0,\"volumetric\":0.4,\"weight\":0.5,\"beer_unit_second_id\":null,\"dateExpir\":{\"year\":2021,\"month\":1,\"day\":31}},{\"beer\":\"1\",\"name\":\"Thùng\",\"price\":200000,\"discount\":10,\"volumetric\":6,\"weight\":0.5,\"beer_unit_second_id\":null,\"dateExpir\":{\"year\":2020,\"month\":12,\"day\":24}}]}";
        beerInfo = new ObjectMapper().readValue(json, BeerSubmitData.class).GetBeerInfo();

        assertThat(beerInfo.getBeer().getBeer_second_id()).isEqualTo("1");
        assertThat(beerInfo.getBeer().getName()).isEqualTo("Bia Nhập Lậu");
        assertThat(beerInfo.getBeer().getDetail()).isEqualTo("Tỉnh lộ 769 từ nút giao Dầu Giây đến quốc lộ 51 sẽ được đầu tư hơn 1.600 tỷ đồng để mở rộng, đón đầu kết nối sân bay Long Thành.\n\nSở Giao thông vận tải Đồng Nai vừa trình UBND tỉnh Đồng Nai phê duyệt đầu tư dự án nâng cấp, mở rộng đường tỉnh 769 đi qua hai huyện Thống Nhất và Long Thành. Dự án là công trình giao thông chiến lược trong 5 năm tới nhằm kết nối các quốc lộ trọng yếu đi qua Đồng Nai như quốc lộ 1A, 20 và 51.");
        assertThat(beerInfo.getBeer().getCategory()).isEqualTo(Beer.Category.HOLOTHURIAN);

        assertThat(beerInfo.getBeerUnit().length).isEqualTo(2);
        assertThat(beerInfo.getBeerUnit()[0].getName()).isEqualTo("Lon");
        assertThat(beerInfo.getBeerUnit()[0].getBeer()).isEqualTo("1");
        assertThat(NgbDateStruct.FromTimestamp(beerInfo.getBeerUnit()[0].getDate_expire())).isEqualTo(NgbDateStruct.builder().day(31).month(1).year(2021).build());
        assertThat(beerInfo.getBeerUnit()[0].getDiscount()).isEqualTo(0);
        assertThat(beerInfo.getBeerUnit()[0].getPrice()).isEqualTo(12000);
        assertThat(beerInfo.getBeerUnit()[0].getVolumetric()).isEqualTo(0.4f);
        assertThat(beerInfo.getBeerUnit()[0].getWeight()).isEqualTo(0.5f);

        assertThat(beerInfo.getBeerUnit()[1].getName()).isEqualTo("Thùng");
        assertThat(beerInfo.getBeerUnit()[1].getBeer()).isEqualTo("1");
        assertThat(NgbDateStruct.FromTimestamp(beerInfo.getBeerUnit()[1].getDate_expire())).isEqualTo(NgbDateStruct.builder().day(24).month(12).year(2020).build());
        assertThat(beerInfo.getBeerUnit()[1].getDiscount()).isEqualTo(10);
        assertThat(beerInfo.getBeerUnit()[1].getPrice()).isEqualTo(200000);
        assertThat(beerInfo.getBeerUnit()[1].getVolumetric()).isEqualTo(6f);
        assertThat(beerInfo.getBeerUnit()[1].getWeight()).isEqualTo(0.5f);
    }


    public void BeerPackageTest() throws JsonProcessingException {
        String json = "{\n" +
                "    \"deviceID\":\"vuong\",\n" +
                "    \"beerID\":\"7788900\",\n" +
                "    \"beerUnits\":[\n" +
                "        {\n" +
                "            \"beerUnitID\":\"333\",\n" +
                "            \"numberUnit\":10\n" +
                "        },\n" +
                "        {\n" +
                "            \"beerUnitID\":\"444\",\n" +
                "            \"numberUnit\":5\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        UserPackage[] userPackages = new ObjectMapper().readValue(json, BeerPackage.class).getUserPackage();

        assertThat(userPackages.length).isEqualTo(2);

        assertThat(userPackages[0].getBeer_id()).isEqualTo("7788900");
        assertThat(userPackages[0].getDevice_id()).isEqualTo("vuong");
        assertThat(userPackages[0].getBeer_unit()).isEqualTo("333");
        assertThat(userPackages[0].getNumber_unit()).isEqualTo(10);

        assertThat(userPackages[1].getBeer_id()).isEqualTo("7788900");
        assertThat(userPackages[1].getDevice_id()).isEqualTo("vuong");
        assertThat(userPackages[1].getBeer_unit()).isEqualTo("444");
        assertThat(userPackages[1].getNumber_unit()).isEqualTo(5);
    }

    public void TestParsingGHN() throws JsonProcessingException {
        String json = "{\"weigitExchange\":0.0002,\"listPackagePriceDetail\":[{\"reciverLocation\":\"INSIDE_REGION\",\"maxWeight\":3,\"priceMaxWeight\":20000,\"nextWeight\":0.5,\"priceNextWeight\":5000},{\"reciverLocation\":\"OUTSIDE_REGION_TYPE1\",\"maxWeight\":3,\"priceMaxWeight\":20000,\"nextWeight\":0.5,\"priceNextWeight\":5000},{\"reciverLocation\":\"OUTSIDE_REGION_TYPE2\",\"maxWeight\":3,\"priceMaxWeight\":20000,\"nextWeight\":0.5,\"priceNextWeight\":5000},{\"reciverLocation\":\"INSIDE_GREGION\",\"maxWeight\":3,\"priceMaxWeight\":20000,\"nextWeight\":0.5,\"priceNextWeight\":5000},{\"reciverLocation\":\"DIFFIRENT_GPREGION\",\"maxWeight\":3,\"priceMaxWeight\":20000,\"nextWeight\":0.5,\"priceNextWeight\":5000}]}";
        ShippingProvider.GHN ghn = new ObjectMapper().readValue(json, ShippingProvider.GHN.class);

        assertThat(ghn.getListPackagePriceDetail().length).isEqualTo(5);

        assertThat(ghn.getWeigitExchange()).isEqualTo(0.0002f);

        assertThat(ghn.getListPackagePriceDetail()[0].getMaxWeight()).isEqualTo(3);
        assertThat(ghn.getListPackagePriceDetail()[0].getReciverLocation()).isEqualTo(ShippingProvider.DistanceType.INSIDE_REGION);
        assertThat(ghn.getListPackagePriceDetail()[0].getPriceMaxWeight()).isEqualTo(20000f);
        assertThat(ghn.getListPackagePriceDetail()[0].getNextWeight()).isEqualTo(0.5f);
        assertThat(ghn.getListPackagePriceDetail()[0].getPriceNextWeight()).isEqualTo(5000f);


        assertThat(ghn.getListPackagePriceDetail()[1].getMaxWeight()).isEqualTo(3);
        assertThat(ghn.getListPackagePriceDetail()[1].getReciverLocation()).isEqualTo(ShippingProvider.DistanceType.OUTSIDE_REGION_TYPE1);
        assertThat(ghn.getListPackagePriceDetail()[1].getPriceMaxWeight()).isEqualTo(20000f);
        assertThat(ghn.getListPackagePriceDetail()[1].getNextWeight()).isEqualTo(0.5f);
        assertThat(ghn.getListPackagePriceDetail()[1].getPriceNextWeight()).isEqualTo(5000f);


        assertThat(ghn.getListPackagePriceDetail()[2].getMaxWeight()).isEqualTo(3);
        assertThat(ghn.getListPackagePriceDetail()[2].getReciverLocation()).isEqualTo(ShippingProvider.DistanceType.OUTSIDE_REGION_TYPE2);
        assertThat(ghn.getListPackagePriceDetail()[2].getPriceMaxWeight()).isEqualTo(20000f);
        assertThat(ghn.getListPackagePriceDetail()[2].getNextWeight()).isEqualTo(0.5f);
        assertThat(ghn.getListPackagePriceDetail()[2].getPriceNextWeight()).isEqualTo(5000f);


        assertThat(ghn.getListPackagePriceDetail()[3].getMaxWeight()).isEqualTo(3);
        assertThat(ghn.getListPackagePriceDetail()[3].getReciverLocation()).isEqualTo(ShippingProvider.DistanceType.INSIDE_GREGION);
        assertThat(ghn.getListPackagePriceDetail()[3].getPriceMaxWeight()).isEqualTo(20000f);
        assertThat(ghn.getListPackagePriceDetail()[3].getNextWeight()).isEqualTo(0.5f);
        assertThat(ghn.getListPackagePriceDetail()[3].getPriceNextWeight()).isEqualTo(5000f);


        assertThat(ghn.getListPackagePriceDetail()[4].getMaxWeight()).isEqualTo(3);
        assertThat(ghn.getListPackagePriceDetail()[4].getReciverLocation()).isEqualTo(ShippingProvider.DistanceType.DIFFIRENT_GPREGION);
        assertThat(ghn.getListPackagePriceDetail()[4].getPriceMaxWeight()).isEqualTo(20000f);
        assertThat(ghn.getListPackagePriceDetail()[4].getNextWeight()).isEqualTo(0.5f);
        assertThat(ghn.getListPackagePriceDetail()[4].getPriceNextWeight()).isEqualTo(5000f);
    }
}
