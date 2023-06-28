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
                            assertThat(district.getId()).isEqualTo(484);
                        }
                )
                .consumeNextWith(district -> {
                            assertThat(district.getName()).isEqualTo("Quận 2");
                            assertThat(district.getId()).isEqualTo(485);
                        }
                )
                .consumeNextWith(district -> {
                            assertThat(district.getName()).isEqualTo("Quận Bình Thạnh");
                            assertThat(district.getId()).isEqualTo(497);
                        }
                )
                .consumeNextWith(district -> {
                            assertThat(district.getName()).isEqualTo("Quận Gò Vấp");
                            assertThat(district.getId()).isEqualTo(498);
                        }
                )
                .consumeNextWith(district -> {
                            assertThat(district.getName()).isEqualTo("Huyện Cần Giờ");
                            assertThat(district.getId()).isEqualTo(504);
                        }
                )
                .consumeNextWith(district -> {
                            assertThat(district.getName()).isEqualTo("Huyện Củ Chi");
                            assertThat(district.getId()).isEqualTo(505);
                        }
                )
                .consumeNextWith(district -> {
                            assertThat(district.getName()).isEqualTo("Huyện Hóc Môn");
                            assertThat(district.getId()).isEqualTo(506);
                        }
                )
                .consumeNextWith(district -> {
                            assertThat(district.getName()).isEqualTo("Huyện Nhà Bè");
                            assertThat(district.getId()).isEqualTo(507);
                        }
                )
                .verifyComplete();

        VietNamAddress.getInstance().GetAllWard(291, 374)
                .as(StepVerifier::create)
                .consumeNextWith(ward -> {
                            assertThat(ward.getName()).isEqualTo("Phường An Hải Bắc");
                            assertThat(ward.getId()).isEqualTo(1896);
                        }
                )
                .consumeNextWith(ward -> {
                            assertThat(ward.getName()).isEqualTo("Phường An Hải Đông");
                            assertThat(ward.getId()).isEqualTo(1897);
                        }
                )
                .consumeNextWith(ward -> {
                            assertThat(ward.getName()).isEqualTo("Phường An Hải Tây");
                            assertThat(ward.getId()).isEqualTo(1898);
                        }
                )
                .consumeNextWith(ward -> {
                            assertThat(ward.getName()).isEqualTo("Phường Mân Thái");
                            assertThat(ward.getId()).isEqualTo(1899);
                        }
                )
                .consumeNextWith(ward -> {
                            assertThat(ward.getName()).isEqualTo("Phường Nại Hiên Đông");
                            assertThat(ward.getId()).isEqualTo(1900);
                        }
                )
                .consumeNextWith(ward -> {
                            assertThat(ward.getName()).isEqualTo("Phường Phước Mỹ");
                            assertThat(ward.getId()).isEqualTo(1901);
                        }
                )
                .consumeNextWith(ward -> {
                            assertThat(ward.getName()).isEqualTo("Phường Thọ Quang");
                            assertThat(ward.getId()).isEqualTo(1902);
                        }
                )
                .verifyComplete();
    }
}
