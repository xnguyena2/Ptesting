package com.example.heroku.services;

import com.example.heroku.model.PackageOrder;
import com.example.heroku.model.repository.BuyerRepository;
import com.example.heroku.model.repository.StatisticBenifitOfProductRepository;
import com.example.heroku.model.repository.StatisticTotalBenifitRepository;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.client.PackageID;
import com.example.heroku.request.client.UserID;
import com.example.heroku.response.BuyerData;
import com.example.heroku.response.BuyerStatictisData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class Buyer {
    @Autowired
    BuyerRepository buyerRepository;

    @Autowired
    StatisticTotalBenifitRepository statisticTotalBenifitRepository;

    @Autowired
    UserPackage userPackageAPI;

    @Autowired
    StatisticBenifitOfProductRepository statisticBenifitOfProductRepository;

    public Mono<com.example.heroku.model.Buyer> insertOrUpdate(com.example.heroku.model.Buyer buyerData, float totalPrice, float realPrice, float shipPrice, float discount, int point) {
        buyerData.updateMetaSearch();
        return buyerRepository.insertOrUpdate(buyerData.getGroup_id(), buyerData.getDevice_id(), totalPrice, realPrice, shipPrice, discount, point, buyerData.getReciver_address(), buyerData.getRegion_id(),
                buyerData.getDistrict_id(), buyerData.getWard_id(), buyerData.getReciver_fullname(), buyerData.getPhone_number(), buyerData.getPhone_number_clean(), buyerData.getMeta_search(),
                buyerData.getStatus(), buyerData.getCreateat());
    }

    public Mono<com.example.heroku.model.Buyer> updatePrice(String groupID, String deviceID, float totalPrice, float realPrice, float shipPrice, float discount, int point){
        return buyerRepository.updateMoney(groupID, deviceID, totalPrice, realPrice, shipPrice, discount, point);
    }

    public Flux<BuyerData> GetAll(SearchQuery query) {
        PackageOrder.Status status = PackageOrder.Status.get(query.GetFilterTxt());
        return this.buyerRepository.getAll(status, query.getGroup_id(), query.getPage(), query.getSize())
                .map(BuyerData::new);
    }

    public Flux<BuyerData> GetAllDirect(SearchQuery query) {
        return this.buyerRepository.findByGroupID(query.getGroup_id(), query.getPage(), query.getSize())
                .map(BuyerData::new);
    }

    public Flux<BuyerData> GetAllDirectWithoutEmpty(SearchQuery query) {
        return this.buyerRepository.findByGroupIDWithoutEmpty(query.getGroup_id(), query.getPage(), query.getSize())
                .map(BuyerData::new);
    }


    public Flux<BuyerData> FindByPhone(SearchQuery query) {
        String phone = "%" + query.getQuery() + "%";
        return this.buyerRepository.findByPhoneOrDeviceIDContains(query.getGroup_id(), phone, query.getPage(), query.getSize())
                .map(BuyerData::new);
    }


    public Mono<BuyerData> FindByPhoneClean(SearchQuery query) {
        String phone = query.getQuery();
        return this.buyerRepository.findByPhoneClean(query.getGroup_id(), phone)
                .map(BuyerData::new);
    }


    public Mono<BuyerData> FindByDeviceID(String groupID, String deviceID) {
        return this.buyerRepository.findByGroupIDAndDeviceID(groupID, deviceID)
                .map(BuyerData::new);
    }


    public Mono<BuyerStatictisData> getBuyerStatictis(PackageID query) {
        BuyerStatictisData buyerStatictisData = BuyerStatictisData.builder().build();
        return statisticTotalBenifitRepository.getTotalStatictisOfBuyer(query.getGroup_id(), query.getDevice_id(), query.getFrom(), query.getTo(), query.getStatus())
                .map(benifitByMonth -> {
                    buyerStatictisData.setBenifitByMonth(benifitByMonth);
                    return buyerStatictisData;
                }).thenMany(
                        userPackageAPI.GetMyPackage(UserID.builder().group_id(query.getGroup_id()).id(query.getDevice_id()).page(query.getPage()).size(query.getSize()).build())
                                .map(buyerStatictisData::addPackge)
                )
                .thenMany(statisticBenifitOfProductRepository.getTotalStatictisOfBuyer(query.getGroup_id(), query.getDevice_id(), query.getFrom(), query.getTo(), query.getStatus(), query.getPage(), query.getSize())
                        .map(buyerStatictisData::addProduct)
                )
                .then(Mono.just(buyerStatictisData));
    }


    public Mono<com.example.heroku.model.Buyer> deleteBuyer(String groupID, String phone) {
        return this.buyerRepository.deleteByPhone(groupID, phone);
    }
}
