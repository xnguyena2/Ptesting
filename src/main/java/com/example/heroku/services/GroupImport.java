package com.example.heroku.services;

import com.example.heroku.model.ProductImport;
import com.example.heroku.model.repository.GroupImportRepository;
import com.example.heroku.model.repository.ProductImportRepository;
import com.example.heroku.model.repository.StatisticWareHouseIncomeOutcomeRepository;
import com.example.heroku.model.statistics.WareHouseIncomeOutCome;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.carousel.IDContainer;
import com.example.heroku.request.warehouse.GroupImportWithItem;
import com.example.heroku.request.warehouse.SearchImportQuery;
import com.example.heroku.response.Format;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@Component
public class GroupImport {

    @Autowired
    GroupImportRepository groupImportRepository;

    @Autowired
    ProductImportRepository productImportRepository;

    @Autowired
    JoinGroupImportWithProductImport joinGroupImportWithProductImport;

    @Autowired
    com.example.heroku.services.Buyer buyer;

    @Autowired
    StatisticWareHouseIncomeOutcomeRepository statisticWareHouseIncomeOutcomeRepository;


    public Mono<ResponseEntity<Format>> SaveGroupImport(GroupImportWithItem groupImportWithItem) {

        if (groupImportWithItem.isEmpty())
            return Mono.just(badRequest().body(Format.builder().response("groupimport empty").build()));
        return
                Mono.just(groupImportWithItem)
                        .flatMap(this::saveSuplier)
                        .flatMap(this::saveGroupDetail)
                        .flatMap(groupImport -> productImportRepository.deleteByGroupID(groupImport.getGroup_id(), groupImport.getGroup_import_second_id())
                                .then(Mono.just(groupImport))
                        )
                        .filter(groupImport -> !groupImport.isEmpty())
                        .flatMapMany(groupImport -> Flux.just(groupImport.getProductImport()))
                        .flatMap(this::saveProductImportItem)
                        .then(Mono.just(ok(Format.builder().response("done").build())));
    }


    public Mono<ResponseEntity<Format>> Return(IDContainer idContaine) {

        return
                groupImportRepository.getByID(idContaine.getGroup_id(), idContaine.getId())
                        .filter(groupImport -> groupImport.getStatus() == ProductImport.Status.CREATE || groupImport.getStatus() == ProductImport.Status.DONE)
                        .flatMap(groupImport -> groupImportRepository.updateStatus(groupImport.getGroup_id(), groupImport.getGroup_import_second_id(), ProductImport.Status.RETURN)
                                .thenMany(
                                        productImportRepository.changeStatus(groupImport.getGroup_id(), groupImport.getGroup_import_second_id(), ProductImport.Status.RETURN)
                                )
                                .then(Mono.just(ok(Format.builder().response("done").build())))
                        );
    }

    public Flux<GroupImportWithItem> GetAllWorking(SearchQuery query) {
        return joinGroupImportWithProductImport.GetAllNotReturn(query);
    }

    public Flux<GroupImportWithItem> GetAllWorkingOfProduct(SearchImportQuery query) {
        return joinGroupImportWithProductImport.GetAllOfProductNotReturn(query);
    }

    public Flux<GroupImportWithItem> GetAllWorkingAndType(SearchImportQuery query) {
        return joinGroupImportWithProductImport.GetAllNotReturnAndType(query);
    }

    public Flux<GroupImportWithItem> GetAllWorkingOfProductAndType(SearchImportQuery query) {
        return joinGroupImportWithProductImport.GetAllOfProductNotReturnAndType(query);
    }

    public Flux<GroupImportWithItem> GetAllWorkingBetween(SearchImportQuery query) {
        return joinGroupImportWithProductImport.GetAllNotReturnBetween(query);
    }

    public Flux<GroupImportWithItem> GetAllWorkingBetweenAndType(SearchImportQuery query) {
        return joinGroupImportWithProductImport.GetAllNotReturnBetweenAndType(query);
    }

    public Flux<GroupImportWithItem> GetAllWorkingOfProductBetweenAndType(SearchImportQuery query) {
        return joinGroupImportWithProductImport.GetAllNotReturnOfProductBetweenAndType(query);
    }

    public Flux<GroupImportWithItem> GetAllWorkingOfProductBetween(SearchImportQuery query) {
        return joinGroupImportWithProductImport.GetAllNotReturnOfProductBetween(query);
    }

    public Flux<GroupImportWithItem> GetByGroupImportID(SearchImportQuery query) {
        return joinGroupImportWithProductImport.GetByGroupImportID(query);
    }

    public Mono<WareHouseIncomeOutCome> GetWareHouseStatictisBetween(SearchImportQuery query){
        return statisticWareHouseIncomeOutcomeRepository.getTotalStatictis(query.getGroup_id(), query.getFrom(), query.getTo(), ProductImport.Status.RETURN);
    }

    private Mono<GroupImportWithItem> saveGroupDetail(GroupImportWithItem productPackage) {
        return saveGroup(productPackage.AutoFill())
                .then(Mono.just(productPackage));
    }

    Mono<GroupImportWithItem> saveSuplier(GroupImportWithItem groupImportWithItem) {
        if (groupImportWithItem.getSupplier_id() == null || groupImportWithItem.getSupplier_id().isEmpty()) {
            return Mono.just(groupImportWithItem);
        }

        com.example.heroku.model.Buyer buyerInfo = com.example.heroku.model.Buyer.builder()
                .group_id(groupImportWithItem.getGroup_id())
                .device_id(groupImportWithItem.getSupplier_id())
                .phone_number(groupImportWithItem.getSupplier_phone())
                .reciver_fullname(groupImportWithItem.getSupplier_name())
                .build();
        buyerInfo.AutoFill(groupImportWithItem.getGroup_id());
        return buyer.insertOrUpdate(buyerInfo, 0, 0, 0, 0, 0).then(Mono.just(groupImportWithItem));
    }

    private Mono<com.example.heroku.model.GroupImport> saveGroup(com.example.heroku.model.GroupImport groupImport) {
        return groupImportRepository.inertOrUpdate(groupImport.getGroup_id(), groupImport.getGroup_import_second_id(), groupImport.getSupplier_id(), groupImport.getSupplier_name(), groupImport.getSupplier_phone(),
                groupImport.getTotal_price(), groupImport.getTotal_amount(), groupImport.getPayment(), groupImport.getDiscount_amount(), groupImport.getAdditional_fee(),
                groupImport.getNote(), groupImport.getImages(), groupImport.getType(), groupImport.getStatus(), groupImport.getCreateat());
    }

    private Mono<ProductImport> saveProductImportItem(ProductImport productImport) {
        return productImportRepository.inertOrUpdate(productImport.getGroup_id(), productImport.getGroup_import_second_id(),
                productImport.getProduct_second_id(), productImport.getProduct_unit_second_id(),
                productImport.getProduct_unit_name_category(), productImport.getPrice(), productImport.getAmount(),
                productImport.getNote(), productImport.getType(), productImport.getStatus(), productImport.getCreateat());
    }
}
