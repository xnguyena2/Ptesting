package com.example.heroku.model.repository;

import com.example.heroku.model.VoucherRelateProduct;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VoucherRelateBeerRepository extends ReactiveCrudRepository<VoucherRelateProduct, Long> {
    @Query(value = "DELETE FROM voucher_relate_product WHERE voucher_relate_product.group_id = :group_id AND voucher_relate_product.voucher_second_id = :id")
    Mono<VoucherRelateProduct> deleteByVoucherSecondId(@Param("group_id")String group_id, @Param("id")String id);

    @Query(value = "SELECT * FROM voucher_relate_product WHERE voucher_relate_product.group_id = :group_id AND voucher_relate_product.voucher_second_id = :id")
    Flux<VoucherRelateProduct> getByVoucherSecondId(@Param("group_id")String group_id, @Param("id")String id);

    @Query(value = "SELECT * FROM voucher_relate_product WHERE voucher_relate_product.group_id = :group_id AND voucher_relate_product.voucher_second_id = :id AND voucher_relate_product.product_second_id = :beer_id")
    Mono<VoucherRelateProduct> getByVoucherSecondIdAndBeerSecondID(@Param("group_id")String group_id, @Param("id")String id, @Param("beer_id")String beerID);
}
