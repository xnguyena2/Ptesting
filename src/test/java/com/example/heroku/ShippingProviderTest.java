package com.example.heroku;

import com.example.heroku.request.ship.ShippingProviderData;
import com.example.heroku.services.ShippingProvider;
import lombok.Builder;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class ShippingProviderTest {

    ShippingProvider shippingProvider;

    String group;
    
    public void ShipTest() throws Exception {
        String json = """
                {
                    "weigitExchange":0.0002,
                    "listPackagePriceDetail":[
                        {
                            "reciverLocation": "INSIDE_REGION",
                            "maxWeight": 3,
                            "priceMaxWeight": 22000,
                            "nextWeight":0.5,
                            "priceNextWeight":5000
                        },
                        {
                            "reciverLocation": "OUTSIDE_REGION_TYPE1",
                            "maxWeight": 1,
                            "priceMaxWeight": 37000,
                            "nextWeight":0.5,
                            "priceNextWeight":6600
                        },
                        {
                            "reciverLocation": "OUTSIDE_REGION_TYPE2",
                            "maxWeight": 1,
                            "priceMaxWeight": 37000,
                            "nextWeight":0.5,
                            "priceNextWeight":6600
                        },
                        {
                            "reciverLocation": "INSIDE_GREGION",
                            "maxWeight": 1,
                            "priceMaxWeight": 37000,
                            "nextWeight":0.5,
                            "priceNextWeight":7000
                        },
                        {
                            "reciverLocation": "DIFFIRENT_GPREGION",
                            "maxWeight": 1,
                            "priceMaxWeight": 37000,
                            "nextWeight":0.5,
                            "priceNextWeight":7700
                        }
                    ]
                }""";
        shippingProvider.CreateShipProvider(ShippingProviderData.builder().id(ShippingProvider.GHN.ID).json(json).group_id(group).build())
                .block();



        shippingProvider.GetShipProvider(group, ShippingProvider.GHN.ID)
                .as(StepVerifier::create)
                .consumeNextWith(shippingProvider -> {
                            assertThat(shippingProvider.getProvider_id()).isEqualTo(ShippingProvider.GHN.ID);
                    assertThat(shippingProvider.getConfig()).isEqualTo(json);
                        }
                )
                .verifyComplete();

        shippingProvider.GetShippingPrice(group, ShippingProvider.GHN.ID, 2.6f, 10000, 294, 484)
                .as(StepVerifier::create)
                .consumeNextWith(price -> {
                            assertThat(price.getPrice()).isEqualTo(22000);
                        }
                )
                .verifyComplete();

        shippingProvider.GetShippingPrice(group, ShippingProvider.GHN.ID, 2.6f, 16000, 294, 484)
                .as(StepVerifier::create)
                .consumeNextWith(price -> {
                            assertThat(price.getPrice()).isEqualTo(27000);
                        }
                )
                .verifyComplete();

        shippingProvider.GetShippingPrice(group, ShippingProvider.GHN.ID, 2.6f, 10000, 291, 371)
                .as(StepVerifier::create)
                .consumeNextWith(price -> {
                            assertThat(price.getPrice()).isEqualTo(67800);
                        }
                )
                .verifyComplete();
    }
}
