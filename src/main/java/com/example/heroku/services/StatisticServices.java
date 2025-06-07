package com.example.heroku.services;

import com.example.heroku.model.UserPackageDetail;
import com.example.heroku.model.repository.*;
import com.example.heroku.model.statistics.*;
import com.example.heroku.request.client.PackageID;
import com.example.heroku.response.BenifitOfOrderAndPaymentTransactionByDate;
import com.example.heroku.response.BenifitOfOrderAndPaymentTransactionByHour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class StatisticServices {

    @Autowired
    StatisticBenifitRepository statisticBenifitRepository;

    @Autowired
    StatisticBenifitByHourRepository statisticBenifitByHourRepository;

    @Autowired
    StatisticTotalBenifitRepository statisticTotalBenifitRepository;

    @Autowired
    StatisticBenifitOfProductRepository statisticBenifitOfProductRepository;

    @Autowired
    StatisticCountOrdersRepository statisticCountOrdersRepository;

    @Autowired
    StatisticBenifitOfBuyerRepository statisticBenifitOfBuyerRepository;

    @Autowired
    StatisticBenifitOfStaffRepository statisticBenifitOfStaffRepository;

    @Autowired
    StatisticBenifitOfOrderRepository statisticBenifitOfOrderRepository;

    @Autowired
    StatisticBenifitPaymentTransactionByDateRepository statisticBenifitPaymentTransactionByDateRepository;

    @Autowired
    StatisticBenifitPaymentTransactionByHourRepository statisticBenifitPaymentTransactionByHourRepository;

    @Autowired
    StatisticBenifitPaymentTransactionByCategoryRepository statisticBenifitPaymentTransactionByCategoryRepository;

    public Mono<BenifitOfOrderAndPaymentTransactionByDate> getBenifitOfPaymentByDateStatictis(PackageID query) {
        BenifitOfOrderAndPaymentTransactionByDate result = BenifitOfOrderAndPaymentTransactionByDate.builder().build();
        return statisticBenifitRepository.getStatictis(query.getGroup_id(), query.getFrom(), query.getTo(), query.getStatus())
                .collectList().map(result::setBenifitByDate)
                .flatMap(benifitOfOrderAndPaymentTransactionByDate -> statisticBenifitPaymentTransactionByDateRepository.getStatictis(query.getGroup_id(), query.getFrom(), query.getTo())
                        .collectList().map(benifitOfOrderAndPaymentTransactionByDate::setBenifitTransactionByDate)
                )
                .flatMap(benifitOfOrderAndPaymentTransactionByDate -> statisticBenifitPaymentTransactionByCategoryRepository.getStatictis(query.getGroup_id(), query.getFrom(), query.getTo())
                        .collectList().map(benifitOfOrderAndPaymentTransactionByDate::setBenifitTransactionCategoryByDate)
                )
                .flatMap(benifitOfOrderAndPaymentTransactionByDate -> getCountCancelReturnStatictis(query).map(countOrderByDate -> benifitOfOrderAndPaymentTransactionByDate.setReturnPrice(countOrderByDate.getRevenue_return())));
    }

    public Mono<BenifitOfOrderAndPaymentTransactionByHour> getBenifitOfPaymentByHourStatictis(PackageID query) {
        BenifitOfOrderAndPaymentTransactionByHour result = BenifitOfOrderAndPaymentTransactionByHour.builder().build();
        return statisticBenifitByHourRepository.getStatictisByHour(query.getGroup_id(), query.getFrom(), query.getTo(), query.getStatus())
                .collectList().map(result::setBenifitByHour)
                .flatMap(benifitOfOrderAndPaymentTransactionByDate -> statisticBenifitPaymentTransactionByHourRepository.getStatictisByHour(query.getGroup_id(), query.getFrom(), query.getTo())
                        .collectList().map(benifitOfOrderAndPaymentTransactionByDate::setBenifitTransactionByHour)
                )
                .flatMap(benifitOfOrderAndPaymentTransactionByDate -> statisticBenifitPaymentTransactionByCategoryRepository.getStatictis(query.getGroup_id(), query.getFrom(), query.getTo())
                        .collectList().map(benifitOfOrderAndPaymentTransactionByDate::setBenifitTransactionCategoryByHour)
                )
                .flatMap(benifitOfOrderAndPaymentTransactionByDate -> getCountCancelReturnStatictis(query).map(countOrderByDate -> benifitOfOrderAndPaymentTransactionByDate.setReturnPrice(countOrderByDate.getRevenue_return())));
    }

    public Flux<BenifitByDate> getPackageStatictis(PackageID query) {
        return statisticBenifitRepository.getStatictis(query.getGroup_id(), query.getFrom(), query.getTo(), query.getStatus());
    }

    public Flux<BenifitByDateHour> getPackageStatictisByHour(PackageID query) {
        return statisticBenifitByHourRepository.getStatictisByHour(query.getGroup_id(), query.getFrom(), query.getTo(), query.getStatus());
    }

    public Mono<BenifitByMonth> getPackageTotalStatictis(PackageID query) {
        return statisticTotalBenifitRepository.getTotalStatictis(query.getGroup_id(), query.getFrom(), query.getTo(), query.getStatus());
    }

    public Flux<BenifitByProduct> getProductBenifitStatictis(PackageID query) {
        return statisticBenifitOfProductRepository.getTotalStatictis(query.getGroup_id(), query.getFrom(), query.getTo(), query.getStatus(), query.getPage(), query.getSize());
    }

    public Mono<CountOrderByDate> getCountCancelReturnStatictis(PackageID query) {
        return statisticCountOrdersRepository.getStatictis(query.getGroup_id(), query.getFrom(), query.getTo(), UserPackageDetail.Status.CANCEL, UserPackageDetail.Status.RETURN);
    }

    public Flux<BenifitByBuyer> getBuyerBenifitStatictis(PackageID query) {
        return statisticBenifitOfBuyerRepository.getTotalStatictis(query.getGroup_id(), query.getFrom(), query.getTo(), query.getStatus(), query.getPage(), query.getSize());
    }

    public Flux<BenifitByBuyer> getStaffBenifitStatictis(PackageID query) {
        return statisticBenifitOfStaffRepository.getTotalStatictis(query.getGroup_id(), query.getFrom(), query.getTo(), query.getStatus(), query.getPage(), query.getSize());
    }

    public Flux<BenifitByOrder> getOrderBenifitStatictis(PackageID query) {
        return statisticBenifitOfOrderRepository.getStatictis(query.getGroup_id(), query.getFrom(), query.getTo(), query.getStatus(), query.getPage(), query.getSize());
    }
}
