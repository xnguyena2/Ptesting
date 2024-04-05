package com.example.heroku.services;

import com.example.heroku.model.ProductImport;
import com.example.heroku.model.UserPackageDetail;
import com.example.heroku.model.repository.GroupImportRepository;
import com.example.heroku.model.repository.ProductImportRepository;
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


    public Mono<ResponseEntity<Format>> SaveGroupImport(GroupImportWithItem groupImportWithItem) {

        if (groupImportWithItem.isEmpty())
            return Mono.just(badRequest().body(Format.builder().response("groupimport empty").build()));
        return
                Mono.just(groupImportWithItem)
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

    private Mono<GroupImportWithItem> saveGroupDetail(GroupImportWithItem productPackage) {
        return saveGroup(productPackage.AutoFill())
                .then(Mono.just(productPackage));
    }

    private Mono<com.example.heroku.model.GroupImport> saveGroup(com.example.heroku.model.GroupImport groupImport) {
        return groupImportRepository.inertOrUpdate(groupImport.getGroup_id(), groupImport.getGroup_import_second_id(), groupImport.getSupplier_id(),
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
