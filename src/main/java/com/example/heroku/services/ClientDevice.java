package com.example.heroku.services;

import com.example.heroku.response.BootStrapData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
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
                        beerAPI.GetAllBeer(0, 24)
                                .map(beerSubmitData ->
                                        bootStrapData.getProducts().add(beerSubmitData)
                                ).then(
                                Mono.just(bootStrapData)
                        )
                )
                .flatMap(bootStrapData ->
                                imageAPI.GetAll("Carousel")
                                        .switchIfEmpty(Mono.just(com.example.heroku.model.Image.builder().build()))
                                        .map(image ->
                                                {
                                                    if(image.getImgid() == null)
                                                        return false;
                                                    return bootStrapData.getCarousel().add(image.getImgid());
                                                }
                                        ).then(Mono.just(bootStrapData))
                )
                .flatMap(bootStrapData ->
                        this.deviceConfigAPI.GetConfig()
                                .switchIfEmpty(Mono.just(com.example.heroku.model.DeviceConfig.builder().color("#333333").build()))
                                .map(bootStrapData::setDeviceConfig)
                );
    }
}
