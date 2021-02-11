package com.example.heroku;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class ClientDeviceTest {

    com.example.heroku.services.ClientDevice clientDeviceAPI;

    public void BootStrapData(){
        clientDeviceAPI.bootStrapData()
                .as(StepVerifier::create)
                .consumeNextWith(bootStrapData -> {
                    try {
                        System.out.println( new ObjectMapper().writeValueAsString(bootStrapData));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat((long) bootStrapData.getCarousel().size()).isEqualTo(4);
                })
                .verifyComplete();
    }
}
