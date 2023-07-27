package com.example.heroku;

import com.example.heroku.request.ship.ShippingProviderData;
import com.example.heroku.services.ShippingProvider;
import lombok.Builder;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class ShippingProviderTest {

    ShippingProvider shippingProvider;
    public void ShipTest() throws Exception {
        String json = "{\n" +
                "    \"weigitExchange\":0.0002,\n" +
                "    \"listPackagePriceDetail\":[\n" +
                "        {\n" +
                "            \"reciverLocation\": \"INSIDE_REGION\",\n" +
                "            \"maxWeight\": 3,\n" +
                "            \"priceMaxWeight\": 22000,\n" +
                "            \"nextWeight\":0.5,\n" +
                "            \"priceNextWeight\":5000\n" +
                "        },\n" +
                "        {\n" +
                "            \"reciverLocation\": \"OUTSIDE_REGION_TYPE1\",\n" +
                "            \"maxWeight\": 1,\n" +
                "            \"priceMaxWeight\": 37000,\n" +
                "            \"nextWeight\":0.5,\n" +
                "            \"priceNextWeight\":6600\n" +
                "        },\n" +
                "        {\n" +
                "            \"reciverLocation\": \"OUTSIDE_REGION_TYPE2\",\n" +
                "            \"maxWeight\": 1,\n" +
                "            \"priceMaxWeight\": 37000,\n" +
                "            \"nextWeight\":0.5,\n" +
                "            \"priceNextWeight\":6600\n" +
                "        },\n" +
                "        {\n" +
                "            \"reciverLocation\": \"INSIDE_GREGION\",\n" +
                "            \"maxWeight\": 1,\n" +
                "            \"priceMaxWeight\": 37000,\n" +
                "            \"nextWeight\":0.5,\n" +
                "            \"priceNextWeight\":7000\n" +
                "        },\n" +
                "        {\n" +
                "            \"reciverLocation\": \"DIFFIRENT_GPREGION\",\n" +
                "            \"maxWeight\": 1,\n" +
                "            \"priceMaxWeight\": 37000,\n" +
                "            \"nextWeight\":0.5,\n" +
                "            \"priceNextWeight\":7700\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        shippingProvider.CreateShipProvider(ShippingProviderData.builder().id(ShippingProvider.GHN.ID).json(json).group_id(Config.group).build())
                .block();



        shippingProvider.GetShipProvider(Config.group, ShippingProvider.GHN.ID)
                .as(StepVerifier::create)
                .consumeNextWith(shippingProvider -> {
                            assertThat(shippingProvider.getProvider_id()).isEqualTo(ShippingProvider.GHN.ID);
                    assertThat(shippingProvider.getConfig()).isEqualTo(json);
                        }
                )
                .verifyComplete();

        shippingProvider.GetShippingPrice(Config.group, ShippingProvider.GHN.ID, 2.6f, 10000, 294, 484)
                .as(StepVerifier::create)
                .consumeNextWith(price -> {
                            assertThat(price.getPrice()).isEqualTo(22000);
                        }
                )
                .verifyComplete();

        shippingProvider.GetShippingPrice(Config.group, ShippingProvider.GHN.ID, 2.6f, 16000, 294, 484)
                .as(StepVerifier::create)
                .consumeNextWith(price -> {
                            assertThat(price.getPrice()).isEqualTo(27000);
                        }
                )
                .verifyComplete();

        shippingProvider.GetShippingPrice(Config.group, ShippingProvider.GHN.ID, 2.6f, 10000, 291, 371)
                .as(StepVerifier::create)
                .consumeNextWith(price -> {
                            assertThat(price.getPrice()).isEqualTo(67800);
                        }
                )
                .verifyComplete();
    }
}
