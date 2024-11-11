package com.example.heroku.services;

import com.example.heroku.model.UserPackageDetail;
import com.example.heroku.model.repository.StatisticTotalBenifitRepository;
import com.example.heroku.model.statistics.BenifitByMonth;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.client.PackageID;
import com.example.heroku.response.BootStrapData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

@Component
public class ClientDevice {
    @Autowired
    Image imageAPI;

    @Autowired
    Beer beerAPI;

    @Autowired
    DeviceConfig deviceConfigAPI;

    @Autowired
    StatisticServices statisticServices;

    @Autowired
    Store storeServices;

    public Mono<BootStrapData> bootStrapDataForWeb(String groupID) {

        return
                this.storeServices.getStoreDomainUrl(groupID)
                        .switchIfEmpty(Mono.just(com.example.heroku.model.Store.builder().group_id(groupID).build()))
                        .map(store -> BootStrapData.builder()
                                .store(store)
                                .carousel(new ArrayList<>())
                                .products(new ArrayList<>())
                                .build()
                        )
                        .flatMap(bootStrapData ->
                                beerAPI.GetAllBeerByJoinFirstForWeb(SearchQuery.builder().group_id(bootStrapData.getStore().getGroup_id()).page(0).size(24).build())
                                        .map(beerSubmitData ->
                                                bootStrapData.getProducts().add(beerSubmitData)
                                        ).then(
                                                Mono.just(bootStrapData)
                                        )
                        )
                        .flatMap(bootStrapData ->
                                imageAPI.GetAll(bootStrapData.getStore().getGroup_id(), "Carousel")
                                        .switchIfEmpty(Mono.just(com.example.heroku.model.Image.builder().build()))
                                        .map(image ->
                                                {
                                                    if (image.getImgid() == null)
                                                        return false;
                                                    return bootStrapData.getCarousel().add(image.getLarge());
                                                }
                                        ).then(Mono.just(bootStrapData))
                        )
                        .flatMap(bootStrapData ->
                                this.deviceConfigAPI.GetConfig(bootStrapData.getStore().getGroup_id())
                                        .switchIfEmpty(Mono.just(com.example.heroku.model.DeviceConfig.builder().color("#333333").build()))
                                        .map(bootStrapData::setDeviceConfig)
                        );
    }

    public Mono<BootStrapData> adminBootStrapWithoutCarouselData(String groupID) {

        return Mono.just(
                        BootStrapData.builder()
                                .carousel(new ArrayList<>())
                                .products(new ArrayList<>())
                                .build())
                .flatMap(bootStrapData ->
                        beerAPI.GetAllBeer(SearchQuery.builder().group_id(groupID).page(0).size(10000).build())
                                .map(beerSubmitData ->
                                        bootStrapData.getProducts().add(beerSubmitData)
                                ).then(
                                        Mono.just(bootStrapData)
                                )
                )
                .flatMap(bootStrapData ->
                        this.deviceConfigAPI.GetConfig(groupID)
                                .switchIfEmpty(Mono.just(com.example.heroku.model.DeviceConfig.builder().group_id(groupID).build()))
                                .map(bootStrapData::setDeviceConfig)
                )
                .flatMap(bootStrapData ->
                        this.storeServices.getStore(groupID)
                                .switchIfEmpty(Mono.just(com.example.heroku.model.Store.builder().build()))
                                .map(bootStrapData::setStore)
                )
                .flatMap(bootStrapData ->
                        this.statisticServices.getPackageTotalStatictis(PackageID.builder()
                                        .group_id(groupID)
                                        .from(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0)))
                                        .to(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"))))
                                        .status(UserPackageDetail.Status.DONE).build())
                                .switchIfEmpty(Mono.just(BenifitByMonth.builder().build()))
                                .map(bootStrapData::setBenifit)
                );
    }

    public Mono<BootStrapData> adminBootStrapWithoutCarouselDataBenifitOfCurrentDate(String groupID) {

        return Mono.just(
                        BootStrapData.builder()
                                .carousel(new ArrayList<>())
                                .products(new ArrayList<>())
                                .build())
                .flatMap(bootStrapData ->
                        beerAPI.GetAllBeerByJoinWithImageFirst(SearchQuery.builder().group_id(groupID).page(0).size(10000).build())
                                .map(beerSubmitData ->
                                        bootStrapData.getProducts().add(beerSubmitData)
                                ).then(
                                        Mono.just(bootStrapData)
                                )
                )
                .flatMap(bootStrapData ->
                        this.deviceConfigAPI.GetConfig(groupID)
                                .switchIfEmpty(Mono.just(com.example.heroku.model.DeviceConfig.builder().group_id(groupID).build()))
                                .map(bootStrapData::setDeviceConfig)
                )
                .flatMap(bootStrapData ->
                        this.storeServices.getStore(groupID)
                                .switchIfEmpty(Mono.just(com.example.heroku.model.Store.builder().build()))
                                .map(bootStrapData::setStore)
                )
                .flatMap(bootStrapData ->
                        this.statisticServices.getPackageTotalStatictis(PackageID.builder()
                                        .group_id(groupID)
                                        .from(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).withHour(0).withMinute(0).withSecond(0).withNano(0)))
                                        .to(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"))))
                                        .status(UserPackageDetail.Status.DONE).build())
                                .switchIfEmpty(Mono.just(BenifitByMonth.builder().build()))
                                .map(bootStrapData::setBenifit)
                );
    }
}
