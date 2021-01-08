package com.example.heroku;

import com.example.heroku.request.beer.BeerPackage;
import com.example.heroku.services.UserPackage;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class UserPackageTest {
    @Autowired
    UserPackage userPackageAPI;

    public void TestUserPackage() {
        BeerPackage beerPackage = BeerPackage.builder().build();
        userPackageAPI.AddBeerToPackage(beerPackage)
                .block();


        beerPackage = BeerPackage.builder()
                .beerID("123")
                .deviceID("222222")
                .beerUnits(new BeerPackage.BeerUnit[]{
                        BeerPackage.BeerUnit.builder()
                                .beerUnitID(1)
                                .numberUnit(100)
                                .build(),
                        BeerPackage.BeerUnit.builder()
                                .beerUnitID(2)
                                .numberUnit(9)
                                .build()
                })
                .build();
        userPackageAPI.AddBeerToPackage(beerPackage)
                .block();

        beerPackage = BeerPackage.builder()
                .beerID("456")
                .deviceID("222222")
                .beerUnits(new BeerPackage.BeerUnit[]{
                        BeerPackage.BeerUnit.builder()
                                .beerUnitID(3)
                                .numberUnit(100)
                                .build(),
                        BeerPackage.BeerUnit.builder()
                                .beerUnitID(3)
                                .numberUnit(9)
                                .build()
                })
                .build();
        userPackageAPI.AddBeerToPackage(beerPackage)
                .block();

        userPackageAPI.GetMyPackage("222222", 0, 1000)
                .as(StepVerifier::create)
                .consumeNextWith(userPackage -> {
                            assertThat(userPackage.getDevice_id()).isEqualTo("222222");
                            assertThat(userPackage.getBeer_id()).isEqualTo("456");
                            assertThat(userPackage.getBeer_unit()).isEqualTo(3);
                            assertThat(userPackage.getNumber_unit()).isEqualTo(109);
                        }
                )
                .consumeNextWith(userPackage -> {
                            assertThat(userPackage.getDevice_id()).isEqualTo("222222");
                            assertThat(userPackage.getBeer_id()).isEqualTo("123");
                            assertThat(userPackage.getBeer_unit()).isEqualTo(1);
                            assertThat(userPackage.getNumber_unit()).isEqualTo(100);
                        }
                )
                .consumeNextWith(userPackage -> {
                            assertThat(userPackage.getDevice_id()).isEqualTo("222222");
                            assertThat(userPackage.getBeer_id()).isEqualTo("123");
                            assertThat(userPackage.getBeer_unit()).isEqualTo(2);
                            assertThat(userPackage.getNumber_unit()).isEqualTo(9);
                        }
                )
                .verifyComplete();
    }
}
