package com.example.heroku.services;

import com.example.heroku.model.ProductImport;
import com.example.heroku.model.repository.GroupImportRepository;
import com.example.heroku.model.repository.ProductImportRepository;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.warehouse.GroupImportWithItem;
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

    public Flux<GroupImportWithItem> GetAllWorking(SearchQuery query) {
        return joinGroupImportWithProductImport.GetAllNotReturn(query);
    }

    public Flux<com.example.heroku.model.ProductImport> getByProductID(SearchQuery query) {
        String dateTxt = query.GetFilterTxt();
        int date = Integer.parseInt(dateTxt);
        return productImportRepository.getAllByProductID(query.getGroup_id(), query.getQuery(), query.getPage(), query.getSize(), date);
    }

    private Mono<GroupImportWithItem> saveGroupDetail(GroupImportWithItem productPackage) {
        return saveGroup(productPackage.AutoFill())
                .then(Mono.just(productPackage));
    }

    private Mono<com.example.heroku.model.GroupImport> saveGroup(com.example.heroku.model.GroupImport groupImport) {
        return groupImportRepository.ínertOrUpdate(groupImport.getGroup_id(), groupImport.getGroup_import_second_id(), groupImport.getSupplier_id(),
                groupImport.getTotal_price(), groupImport.getTotal_amount(), groupImport.getDiscount_amount(), groupImport.getAdditional_fee(),
                groupImport.getNote(), groupImport.getImages(), groupImport.getType(), groupImport.getStatus(), groupImport.getCreateat());
    }

    private Mono<ProductImport> saveProductImportItem(ProductImport productImport) {
        return productImportRepository.ínertOrUpdate(productImport.getGroup_id(), productImport.getGroup_import_second_id(), productImport.getProduct_import_second_id(),
                productImport.getProduct_second_id(), productImport.getProduct_unit_second_id(),
                productImport.getProduct_unit_name_category(), productImport.getPrice(), productImport.getAmount(),
                productImport.getNote(), productImport.getType(), productImport.getStatus(), productImport.getCreateat());
    }
}
