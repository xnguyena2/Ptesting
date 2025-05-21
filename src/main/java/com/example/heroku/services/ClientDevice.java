package com.example.heroku.services;

import com.example.heroku.model.UserPackageDetail;
import com.example.heroku.model.statistics.BenifitByMonth;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.client.PackageID;
import com.example.heroku.response.BootStrapData;
import com.example.heroku.response.BootStrapDataWeb;
import com.example.heroku.response.Format;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

import static org.springframework.http.ResponseEntity.ok;

@Component
public class ClientDevice {

    private final String WEB_CONFIG = "webconfig";

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

    @Autowired
    private MapKeyValue mapKeyValue;

    public Mono<BootStrapDataWeb> bootStrapDataForWeb(String groupID) {

        return
                this.storeServices.getStoreDomainUrl(groupID)
                        .switchIfEmpty(Mono.just(com.example.heroku.model.Store.builder().group_id(groupID).build()))
                        .map(store -> (BootStrapDataWeb) BootStrapDataWeb.builder()
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
                                        .then(Mono.just(bootStrapData))
                        )
                        .flatMap(bootStrapData ->
                                this.mapKeyValue.getByID(bootStrapData.getStore().getGroup_id(), WEB_CONFIG)
                                        .switchIfEmpty(Mono.just(com.example.heroku.model.MapKeyValue.builder().build()))
                                        .map(mapKeyValue -> {
                                            if (mapKeyValue.getValue_o() == null) {
                                                return bootStrapData;
                                            }
                                            return bootStrapData.setWeb_config(mapKeyValue.getValue_o());
                                        })
                        );

    }

    public Mono<ResponseEntity<Format>> saveWebConfig(com.example.heroku.model.MapKeyValue webconfig) {
        return this.mapKeyValue.insert(com.example.heroku.model.MapKeyValue.builder().group_id(webconfig.getGroup_id()).id_o(WEB_CONFIG).value_o(webconfig.getValue_o()).build())
                .map(com.example.heroku.model.MapKeyValue::AutoFill)
                .then(
                        Mono.just(ok(Format.builder().response(webconfig.getValue_o()).build()))
                );
    }

    public Mono<ResponseEntity<Format>> getWebConfig(String groupID) {
        return this.mapKeyValue.getByID(groupID, WEB_CONFIG)
                .map(mapKeyValue ->
                        ok(Format.builder().response(mapKeyValue.getValue_o()).build())
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
