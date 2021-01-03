package com.example.heroku;

import com.example.heroku.model.Beer;
import com.example.heroku.request.beer.BeerInfo;
import com.example.heroku.request.beer.BeerSubmitData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;

import java.sql.Timestamp;
import java.util.Calendar;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class ObjectPraserTest {

    public void BeerSubmitDataTest() throws JsonProcessingException {
        String json = "{\"name\":\"Bia Trung Quốc\",\"beerSecondID\":\"1\",\"detail\":\"Vành đai 1 đoạn Hoàng Cầu - Voi Phục dài 2,27 km, tổng đầu tư hơn 7.200 tỷ đồng, do Ban Quản lý dự án đầu tư xây dựng công trình dân dụng và công nghiệp thành phố đang triển khai bằng nguồn vốn ngân sách TP Hà Nội.\\n\\nVành đai 2 trên cao, đoạn Vĩnh Tuy - Ngã Tư Vọng kết hợp mở rộng phần từ Vĩnh Tuy đến Ngã Tư Sở đang thực hiện theo hợp đồng BT (xây dựng - chuyển giao) ký kết giữa Hà Nội và Tập đoàn Vingroup.\",\"category\":\"fresh_water\",\"listUnit\":[{\"beer\":\"1\",\"name\":\"Lon\",\"price\":17000,\"discount\":null,\"shipPrice\":\"0.33(lit)\",\"dateExpir\":null}]}";

        BeerInfo beerInfo = new ObjectMapper().readValue(json, BeerSubmitData.class).GetBeerInfo();

        assertThat(beerInfo.getBeer().getBeer_second_id()).isEqualTo("1");
        assertThat(beerInfo.getBeer().getName()).isEqualTo("Bia Trung Quốc");
        assertThat(beerInfo.getBeer().getDetail()).isEqualTo("Vành đai 1 đoạn Hoàng Cầu - Voi Phục dài 2,27 km, tổng đầu tư hơn 7.200 tỷ đồng, do Ban Quản lý dự án đầu tư xây dựng công trình dân dụng và công nghiệp thành phố đang triển khai bằng nguồn vốn ngân sách TP Hà Nội.\n\nVành đai 2 trên cao, đoạn Vĩnh Tuy - Ngã Tư Vọng kết hợp mở rộng phần từ Vĩnh Tuy đến Ngã Tư Sở đang thực hiện theo hợp đồng BT (xây dựng - chuyển giao) ký kết giữa Hà Nội và Tập đoàn Vingroup.");
        assertThat(beerInfo.getBeer().getCategory()).isEqualTo(Beer.Category.FRESH_WATER);

        assertThat(beerInfo.getBeerUnit().length).isEqualTo(1);
        assertThat(beerInfo.getBeerUnit()[0].getName()).isEqualTo("Lon");
        assertThat(beerInfo.getBeerUnit()[0].getBeer()).isEqualTo("1");
        assertThat(beerInfo.getBeerUnit()[0].getDate_expire()).isEqualTo(null);
        assertThat(beerInfo.getBeerUnit()[0].getDiscount()).isEqualTo(0);
        assertThat(beerInfo.getBeerUnit()[0].getPrice()).isEqualTo(17000);
        assertThat(beerInfo.getBeerUnit()[0].getShip_price()).isEqualTo("0.33(lit)");

        json = "{\"beerSecondID\":\"1\",\"listUnit\":[{\"beer\":\"1\",\"name\":\"\",\"price\":0,\"discount\":0,\"shipPrice\":\"\",\"dateExpir\":null}]}";
        beerInfo = new ObjectMapper().readValue(json, BeerSubmitData.class).GetBeerInfo();

        assertThat(beerInfo.getBeerUnit().length).isEqualTo(1);
        assertThat(beerInfo.getBeerUnit()[0].getBeer()).isEqualTo("1");
        assertThat(beerInfo.getBeerUnit()[0].getDate_expire()).isEqualTo(null);
        assertThat(beerInfo.getBeerUnit()[0].getDiscount()).isEqualTo(0);
        assertThat(beerInfo.getBeerUnit()[0].getPrice()).isEqualTo(0);
        assertThat(beerInfo.getBeerUnit()[0].getShip_price()).isEqualTo("");

        json = "{\"name\":\"Bia Nhập Lậu\",\"beerSecondID\":\"1\",\"detail\":\"Tỉnh lộ 769 từ nút giao Dầu Giây đến quốc lộ 51 sẽ được đầu tư hơn 1.600 tỷ đồng để mở rộng, đón đầu kết nối sân bay Long Thành.\\n\\nSở Giao thông vận tải Đồng Nai vừa trình UBND tỉnh Đồng Nai phê duyệt đầu tư dự án nâng cấp, mở rộng đường tỉnh 769 đi qua hai huyện Thống Nhất và Long Thành. Dự án là công trình giao thông chiến lược trong 5 năm tới nhằm kết nối các quốc lộ trọng yếu đi qua Đồng Nai như quốc lộ 1A, 20 và 51.\",\"category\":\"internationnal_drinks\",\"listUnit\":[{\"beer\":\"1\",\"name\":\"Lon\",\"price\":12000,\"discount\":0,\"shipPrice\":\"0.4(kg)\",\"dateExpir\":{\"year\":2021,\"month\":1,\"day\":31}},{\"beer\":\"1\",\"name\":\"Thùng\",\"price\":200000,\"discount\":10,\"shipPrice\":\"6(kg)\",\"dateExpir\":{\"year\":2020,\"month\":12,\"day\":24}}]}";
        beerInfo = new ObjectMapper().readValue(json, BeerSubmitData.class).GetBeerInfo();

        assertThat(beerInfo.getBeer().getBeer_second_id()).isEqualTo("1");
        assertThat(beerInfo.getBeer().getName()).isEqualTo("Bia Nhập Lậu");
        assertThat(beerInfo.getBeer().getDetail()).isEqualTo("Tỉnh lộ 769 từ nút giao Dầu Giây đến quốc lộ 51 sẽ được đầu tư hơn 1.600 tỷ đồng để mở rộng, đón đầu kết nối sân bay Long Thành.\n\nSở Giao thông vận tải Đồng Nai vừa trình UBND tỉnh Đồng Nai phê duyệt đầu tư dự án nâng cấp, mở rộng đường tỉnh 769 đi qua hai huyện Thống Nhất và Long Thành. Dự án là công trình giao thông chiến lược trong 5 năm tới nhằm kết nối các quốc lộ trọng yếu đi qua Đồng Nai như quốc lộ 1A, 20 và 51.");
        assertThat(beerInfo.getBeer().getCategory()).isEqualTo(Beer.Category.INTERNATIONNAL_DRINKS);

        assertThat(beerInfo.getBeerUnit().length).isEqualTo(2);
        assertThat(beerInfo.getBeerUnit()[0].getName()).isEqualTo("Lon");
        assertThat(beerInfo.getBeerUnit()[0].getBeer()).isEqualTo("1");
        assertThat(beerInfo.getBeerUnit()[0].getDate_expire()).isEqualTo(GetExpirDateTime(2021,1,31));
        assertThat(beerInfo.getBeerUnit()[0].getDiscount()).isEqualTo(0);
        assertThat(beerInfo.getBeerUnit()[0].getPrice()).isEqualTo(12000);
        assertThat(beerInfo.getBeerUnit()[0].getShip_price()).isEqualTo("0.4(kg)");

        assertThat(beerInfo.getBeerUnit()[1].getName()).isEqualTo("Thùng");
        assertThat(beerInfo.getBeerUnit()[1].getBeer()).isEqualTo("1");
        assertThat(beerInfo.getBeerUnit()[1].getDate_expire()).isEqualTo(GetExpirDateTime(2020,12,24));
        assertThat(beerInfo.getBeerUnit()[1].getDiscount()).isEqualTo(10);
        assertThat(beerInfo.getBeerUnit()[1].getPrice()).isEqualTo(200000);
        assertThat(beerInfo.getBeerUnit()[1].getShip_price()).isEqualTo("6(kg)");
    }

    public Timestamp GetExpirDateTime(int year, int month, int date) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, date);
        return new Timestamp(cal.getTime().getTime());
    }

}
