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
    Beer beerAPI;

    @Autowired
    DeviceConfig deviceConfigAPI;

    public Mono<BootStrapData> bootStrapData() {

        return Mono.just(
                BootStrapData.builder()
                        .carousel(new ArrayList<>())
                        .products(new ArrayList<>())
                        .build())
                .flatMap(bootStrapData ->
                        beerAPI.GetAllBeer(0, 25)
                                .map(beerSubmitData ->
                                        bootStrapData.getProducts().add(beerSubmitData)
                                ).then(
                                Mono.just(bootStrapData)
                                        .flatMap(data ->
                                                this.deviceConfigAPI.GetConfig())
                                        .map(bootStrapData::setDeviceConfig)
                        )
                )
                .flatMap(bootStrapData ->
                        imageAPI.GetAll("Carousel")
                                .map(image ->
                                        bootStrapData.getCarousel().add(image.getImgid())
                                ).then(
                                Mono.just(bootStrapData)
                        )
                );
    }
}
