package com.example.heroku;

import com.example.heroku.services.VietNamAddress;
import lombok.Builder;
import reactor.test.StepVerifier;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class SaveVietNamAddressTest {

    public void SaveToDataBaseTest() throws IOException {

        VietNamAddress.getInstance().SaveAllToDatabase(true);

        VietNamAddress.getInstance().GetAllRegion()
                .as(StepVerifier::create)
                .consumeNextWith(region -> {
                            assertThat(region.getName()).isEqualTo("Hồ Chí Minh");
                        }
                )
                .consumeNextWith(region -> {
                            assertThat(region.getName()).isEqualTo("Hà Nội");
                        }
                )
                .consumeNextWith(region -> {
                            assertThat(region.getName()).isEqualTo("Đà Nẵng");
                        }
                )
                .verifyComplete();

        VietNamAddress.getInstance().GetAllRegionFormat()
                .as(StepVerifier::create)
                .consumeNextWith(region -> {
                            assertThat(region.getName()).isEqualTo("Hồ Chí Minh");
                        }
                )
                .consumeNextWith(region -> {
                            assertThat(region.getName()).isEqualTo("Hà Nội");
                        }
                )
                .consumeNextWith(region -> {
                            assertThat(region.getName()).isEqualTo("Đà Nẵng");
                        }
                )
                .verifyComplete();

        VietNamAddress.getInstance().GetAllDistrict(294)
                .as(StepVerifier::create)
                .consumeNextWith(district -> {
                            assertThat(district.getName()).isEqualTo("Quận 1");
                        }
                )
                .consumeNextWith(district -> {
                            assertThat(district.getName()).isEqualTo("Quận 2");
                        }
                )
                .consumeNextWith(district -> {
                            assertThat(district.getName()).isEqualTo("Quận Bình Thạnh");
                        }
                )
                .consumeNextWith(district -> {
                            assertThat(district.getName()).isEqualTo("Quận Gò Vấp");
                        }
                )
                .consumeNextWith(district -> {
                            assertThat(district.getName()).isEqualTo("Huyện Cần Giờ");
                        }
                )
                .consumeNextWith(district -> {
                            assertThat(district.getName()).isEqualTo("Huyện Củ Chi");
                        }
                )
                .consumeNextWith(district -> {
                            assertThat(district.getName()).isEqualTo("Huyện Hóc Môn");
                        }
                )
                .consumeNextWith(district -> {
                            assertThat(district.getName()).isEqualTo("Huyện Nhà Bè");
                        }
                )
                .verifyComplete();

        VietNamAddress.getInstance().GetAllDistrictFormat(294)
                .as(StepVerifier::create)
                .consumeNextWith(district -> {
                            assertThat(district.getName()).isEqualTo("Quận 1");
                        }
                )
                .consumeNextWith(district -> {
                            assertThat(district.getName()).isEqualTo("Quận 2");
                        }
                )
                .consumeNextWith(district -> {
                            assertThat(district.getName()).isEqualTo("Quận Bình Thạnh");
                        }
                )
                .consumeNextWith(district -> {
                            assertThat(district.getName()).isEqualTo("Quận Gò Vấp");
                        }
                )
                .consumeNextWith(district -> {
                            assertThat(district.getName()).isEqualTo("Huyện Cần Giờ");
                        }
                )
                .consumeNextWith(district -> {
                            assertThat(district.getName()).isEqualTo("Huyện Củ Chi");
                        }
                )
                .consumeNextWith(district -> {
                            assertThat(district.getName()).isEqualTo("Huyện Hóc Môn");
                        }
                )
                .consumeNextWith(district -> {
                            assertThat(district.getName()).isEqualTo("Huyện Nhà Bè");
                        }
                )
                .verifyComplete();

        VietNamAddress.getInstance().GetAllWard(291, 374)
                .as(StepVerifier::create)
                .consumeNextWith(ward -> {
                            assertThat(ward.getName()).isEqualTo("Phường An Hải Bắc");
                        }
                )
                .consumeNextWith(ward -> {
                            assertThat(ward.getName()).isEqualTo("Phường An Hải Đông");
                        }
                )
                .consumeNextWith(ward -> {
                            assertThat(ward.getName()).isEqualTo("Phường An Hải Tây");
                        }
                )
                .consumeNextWith(ward -> {
                            assertThat(ward.getName()).isEqualTo("Phường Mân Thái");
                        }
                )
                .consumeNextWith(ward -> {
                            assertThat(ward.getName()).isEqualTo("Phường Nại Hiên Đông");
                        }
                )
                .consumeNextWith(ward -> {
                            assertThat(ward.getName()).isEqualTo("Phường Phước Mỹ");
                        }
                )
                .consumeNextWith(ward -> {
                            assertThat(ward.getName()).isEqualTo("Phường Thọ Quang");
                        }
                )
                .verifyComplete();
    }
}
