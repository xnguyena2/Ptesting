package com.example.heroku.services;

import com.example.heroku.model.UserPackageDetail;
import com.example.heroku.model.statistics.BenifitByMonth;
import com.example.heroku.request.beer.BeerSubmitData;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.carousel.IDContainer;
import com.example.heroku.request.client.PackageID;
import com.example.heroku.response.BootStrapData;
import com.example.heroku.response.BootStrapDataWeb;
import com.example.heroku.response.Format;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private ProductSerial productSerialAPI;

    public Mono<BootStrapDataWeb> bootStrapDataForWeb(String domainID) {
        // Lấy store
        return storeServices.getStoreDomainUrl(domainID)
                .defaultIfEmpty(com.example.heroku.model.Store.builder().domain_url(domainID).group_id(domainID).build()) // ✅ fallback nếu không có store
                .flatMap(store -> {

                    String groupID = store.getGroup_id();

                    // Lấy sản phẩm
                    Mono<List<BeerSubmitData>> productMono = beerAPI.GetAllBeerByJoinFirstForWeb(
                                    SearchQuery.builder().
                                            group_id(groupID).page(0).size(24).build()
                            )
                            .collectList()
                            .defaultIfEmpty(new ArrayList<>()); // ✅ đảm bảo không empty

                    Mono<List<String>> carouselMono = imageAPI.GetAll(groupID, "Carousel")
                            .map(com.example.heroku.model.Image::getLarge)
                            .collectList()
                            .defaultIfEmpty(new ArrayList<>()); // ✅ đảm bảo không empty

                    // Lấy device config
                    Mono<com.example.heroku.model.DeviceConfig> configMono = this.deviceConfigAPI.GetConfig(groupID)
                            .defaultIfEmpty(com.example.heroku.model.DeviceConfig.builder().group_id(groupID).color("#333333").build()); // ✅ fallback nếu không có config


                    Mono<String> webConfigMono = this.mapKeyValue.getByID(groupID, WEB_CONFIG)
                            .map(com.example.heroku.model.MapKeyValue::getValue_o)
                            .defaultIfEmpty(""); // ✅ fallback nếu không có web config

                    Mono<List<com.example.heroku.model.ProductSerial>> productSerialMono = productSerialAPI
                            .getAllSerial(IDContainer.builder().group_id(groupID).build())
                            .collectList()
                            .defaultIfEmpty(new ArrayList<>()); // ✅ đảm bảo không empty


                    return Mono.zip(productMono, carouselMono, configMono, Mono.just(store), webConfigMono, productSerialMono)
                            .map(tuple -> BootStrapDataWeb.builder()
                                    .products(tuple.getT1())
                                    .carousel(tuple.getT2())
                                    .deviceConfig(tuple.getT3())
                                    .store(tuple.getT4())
                                    .web_config(tuple.getT5())
                                    .productSerials(tuple.getT6())
                                    .build()
                            );

                });

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
        Mono<List<BeerSubmitData>> productMono = beerAPI
                .GetAllBeer(SearchQuery.builder().group_id(groupID).page(0).size(10000).build())
                .collectList()
                .defaultIfEmpty(List.of());

        Mono<com.example.heroku.model.DeviceConfig> configMono = deviceConfigAPI
                .GetConfig(groupID)
                .defaultIfEmpty(com.example.heroku.model.DeviceConfig.builder().group_id(groupID).build());

        Mono<com.example.heroku.model.Store> storeMono = storeServices
                .getStore(groupID)
                .defaultIfEmpty(com.example.heroku.model.Store.builder().build());


        // Thiết lập múi giờ Việt Nam
        ZoneId vietnamZone = ZoneId.of("Asia/Ho_Chi_Minh");

        // 1. Start of month in Vietnam
        LocalDateTime startOfMonthVN = LocalDate.now(vietnamZone)
                .withDayOfMonth(1)  // go to 1st day of month
                .atStartOfDay();    // midnight

        // 2. Current time in Vietnam
        LocalDateTime nowVN = LocalDateTime.now(vietnamZone);

        // 3. Convert both to UTC LocalDateTime
        LocalDateTime startOfMonthUTC  = startOfMonthVN
                .atZone(vietnamZone)         // attach Vietnam zone
                .withZoneSameInstant(ZoneOffset.UTC) // shift to UTC
                .toLocalDateTime();

        LocalDateTime nowUTC = nowVN
                .atZone(vietnamZone)
                .withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime();
        Mono<BenifitByMonth> benifitMono = statisticServices
                .getPackageTotalStatictis(PackageID.builder()
                        .group_id(groupID)
                        .from(startOfMonthUTC)
                        .to(nowUTC)
                        .status(UserPackageDetail.Status.DONE)
                        .build())
                .defaultIfEmpty(BenifitByMonth.builder().build());

        Mono<List<com.example.heroku.model.ProductSerial>> productSerialMono = productSerialAPI
                .getAllSerial(IDContainer.builder().group_id(groupID).build())
                .collectList()
                .defaultIfEmpty(new ArrayList<>()); // ✅ đảm bảo không empty

        return Mono.zip(productMono, configMono, storeMono, benifitMono, productSerialMono)
                .map(tuple -> BootStrapData.builder()
                        .products(List.copyOf(tuple.getT1()))
                        .deviceConfig(tuple.getT2())
                        .store(tuple.getT3())
                        .benifit(tuple.getT4())
                        .carousel(List.of()) // Không có carousel
                        .productSerials(tuple.getT5())
                        .build());
    }

    public Mono<BootStrapData> adminBootStrapWithoutCarouselDataBenifitOfCurrentDate(String groupID) {

        // Lấy sản phẩm
        Mono<List<BeerSubmitData>> productMono = beerAPI
                .GetAllBeerByJoinWithImageFirst(SearchQuery.builder()
                        .group_id(groupID)
                        .page(0)
                        .size(10000)
                        .build())
                .collectList()
                .defaultIfEmpty(new ArrayList<>()); // ✅ đảm bảo không empty

        // Lấy device config
        Mono<com.example.heroku.model.DeviceConfig> configMono = deviceConfigAPI
                .GetConfig(groupID)
                .defaultIfEmpty(com.example.heroku.model.DeviceConfig.builder().group_id(groupID).build()); // ✅ fallback nếu không có config

        // Lấy store
        Mono<com.example.heroku.model.Store> storeMono = storeServices
                .getStore(groupID)
                .defaultIfEmpty(com.example.heroku.model.Store.builder().group_id(groupID).build()); // ✅ fallback nếu không có store



        // Thiết lập múi giờ Việt Nam
        ZoneId vietnamZone = ZoneId.of("Asia/Ho_Chi_Minh");

        // 1. Start of today in Vietnam
        LocalDateTime startOfDayVN = LocalDate.now(vietnamZone).atStartOfDay();

        // 2. Current time in Vietnam
        LocalDateTime nowVN = LocalDateTime.now(vietnamZone);

        // 3. Convert both to UTC LocalDateTime
        LocalDateTime startOfDayUTC = startOfDayVN
                .atZone(vietnamZone)         // attach Vietnam zone
                .withZoneSameInstant(ZoneOffset.UTC) // shift to UTC
                .toLocalDateTime();

        LocalDateTime nowUTC = nowVN
                .atZone(vietnamZone)
                .withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime();
        // Lấy thống kê lợi nhuận hôm nay
        Mono<BenifitByMonth> benifitMono = statisticServices
                .getPackageTotalStatictis(PackageID.builder()
                        .group_id(groupID)
                        .from(startOfDayUTC)
                        .to(nowUTC)
                        .status(UserPackageDetail.Status.DONE).build())
                .defaultIfEmpty(BenifitByMonth.builder().build()); // ✅ fallback nếu không có thống kê

        Mono<List<com.example.heroku.model.ProductSerial>> productSerialMono = productSerialAPI
                .getAllSerial(IDContainer.builder().group_id(groupID).build())
                .collectList()
                .defaultIfEmpty(new ArrayList<>()); // ✅ đảm bảo không empty


        return Mono.zip(productMono, configMono, storeMono, benifitMono, productSerialMono)
                .map(tuple -> BootStrapData.builder()
                        .products(tuple.getT1())
                        .deviceConfig(tuple.getT2())
                        .store(tuple.getT3())
                        .benifit(tuple.getT4())
                        .carousel(List.of()) // Không có carousel
                        .productSerials(tuple.getT5())
                        .build());
    }
}
