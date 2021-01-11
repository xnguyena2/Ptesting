package com.example.heroku.model.repository;

import com.example.heroku.model.VoucherRelateBeer;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VoucherRelateBeerRepository extends ReactiveCrudRepository<VoucherRelateBeer, String> {
    @Query(value = "DELETE FROM voucher_relate_beer WHERE voucher_relate_beer.voucher_second_id = :id")
    Mono<VoucherRelateBeer> deleteByVoucherSecondId(@Param("id")String id);

    @Query(value = "SELECT * FROM voucher_relate_beer WHERE voucher_relate_beer.voucher_second_id = :id")
    Flux<VoucherRelateBeer> getByVoucherSecondId(@Param("id")String id);

    @Query(value = "SELECT * FROM voucher_relate_beer WHERE voucher_relate_beer.voucher_second_id = :id AND voucher_relate_beer.beer_second_id = :beer_id")
    Mono<VoucherRelateBeer> getByVoucherSecondIdAndBeerSecondID(@Param("id")String id, @Param("beer_id")String beerID);
}
