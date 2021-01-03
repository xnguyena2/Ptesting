package com.example.heroku.services;

import com.example.heroku.response.BootStrapData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Component
public class ClientDevice {
    @Autowired
    Image imageAPI;

    @Autowired
    DeviceConfig deviceConfigAPI;

    public Mono<BootStrapData> bootStrapData(){

        return Mono.just(
                BootStrapData.builder()
                        .listCarousel(new ArrayList<>())
                        .build()
        )
                .flatMap(bootStrapData ->
                                imageAPI.GetAll("Carousel")
                                        .map(image ->
                                                bootStrapData.getListCarousel().add(image.getId())
                                        ).then(
                                        Mono.just(bootStrapData)
                                                .flatMap(data->
                                                        this.deviceConfigAPI.GetConfig())
                                                .map(deviceConfig -> bootStrapData.setDeviceConfig(deviceConfig))
                                )
                        //.map(save-> ok(Format.builder().response(Util.getInstance().GenerateID()).build()))
                );
    }
}
