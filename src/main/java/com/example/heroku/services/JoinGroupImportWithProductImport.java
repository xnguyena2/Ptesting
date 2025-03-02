package com.example.heroku.services;

import com.example.heroku.model.ProductImport;
import com.example.heroku.model.joinwith.GroupImportJoinProductImport;
import com.example.heroku.model.repository.JoinGroupImportWithProductImportRepository;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.warehouse.GroupImportWithItem;
import com.example.heroku.request.warehouse.SearchImportQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class JoinGroupImportWithProductImport {

    @Autowired
    JoinGroupImportWithProductImportRepository joinGroupImportWithProductImportRepository;


    private Flux<GroupImportWithItem> flattenResult(Flux<GroupImportJoinProductImport> joinResult) {
        return joinResult.groupBy(GroupImportJoinProductImport::getGroup_import_second_id)
                .flatMap(groupedFlux -> groupedFlux.collectList().map(GroupImportJoinProductImport::GenerateGroupImportWithItem));
    }

    public Flux<GroupImportWithItem> GetAllNotReturn(SearchQuery query) {
        return flattenResult(this.joinGroupImportWithProductImportRepository.getGroupIfNotStatus(query.getGroup_id(), ProductImport.Status.RETURN, query.getPage(), query.getSize()));
    }

    public Flux<GroupImportWithItem> GetAllOfProductNotReturn(SearchImportQuery query) {
        return flattenResult(this.joinGroupImportWithProductImportRepository.getGroupByProductIfNotStatus(query.getGroup_id(), query.getProduct_second_id(), query.getProduct_unit_second_id(), ProductImport.Status.RETURN, query.getPage(), query.getSize()));
    }

    public Flux<GroupImportWithItem> GetAllNotReturnAndType(SearchImportQuery query) {
        return flattenResult(this.joinGroupImportWithProductImportRepository.getGroupIfNotStatusAndType(query.getGroup_id(), ProductImport.Status.RETURN, query.getType(), query.getPage(), query.getSize()));
    }

    public Flux<GroupImportWithItem> GetAllOfProductNotReturnAndType(SearchImportQuery query) {
        return flattenResult(this.joinGroupImportWithProductImportRepository.getGroupByProductIfNotStatusAndType(query.getGroup_id(), query.getProduct_second_id(), query.getProduct_unit_second_id(), ProductImport.Status.RETURN, query.getType(), query.getPage(), query.getSize()));
    }

    public Flux<GroupImportWithItem> GetAllNotReturnBetween(SearchImportQuery query) {
        return flattenResult(this.joinGroupImportWithProductImportRepository.getGroupIfNotStatusBetween(query.getGroup_id(), query.getFrom(), query.getTo(), ProductImport.Status.RETURN, query.getPage(), query.getSize()));
    }

    public Flux<GroupImportWithItem> GetAllNotReturnBetweenAndType(SearchImportQuery query) {
        return flattenResult(this.joinGroupImportWithProductImportRepository.getGroupIfNotStatusBetweenAndType(query.getGroup_id(), query.getFrom(), query.getTo(), ProductImport.Status.RETURN, query.getType(), query.getPage(), query.getSize()));
    }

    public Flux<GroupImportWithItem> GetAllNotReturnOfProductBetweenAndType(SearchImportQuery query) {
        return flattenResult(this.joinGroupImportWithProductImportRepository.getGroupByProductIfNotStatusBetweenAndType(query.getGroup_id(), query.getProduct_second_id(), query.getProduct_unit_second_id(), query.getFrom(), query.getTo(), ProductImport.Status.RETURN, query.getType(), query.getPage(), query.getSize()));
    }

    public Flux<GroupImportWithItem> GetAllNotReturnOfProductBetween(SearchImportQuery query) {
        return flattenResult(this.joinGroupImportWithProductImportRepository.getGroupByProductIfNotStatusBetween(query.getGroup_id(), query.getProduct_second_id(), query.getProduct_unit_second_id(), query.getFrom(), query.getTo(), ProductImport.Status.RETURN, query.getPage(), query.getSize()));
    }

    public Flux<GroupImportWithItem> GetByGroupImportID(SearchImportQuery query) {
        return flattenResult(this.joinGroupImportWithProductImportRepository.getByGroupID(query.getGroup_id(), query.getGroup_import_second_id()));
    }

    public Flux<GroupImportWithItem> GetAllGroupImport(SearchQuery query) {
        return flattenResult(this.joinGroupImportWithProductImportRepository.getAll(query.getGroup_id(), query.getPage(), query.getSize()));
    }

    public Flux<GroupImportWithItem> GetAllDebtOfSupplier(String groupID, ProductImport.ImportType importType, String supplier) {
        if (importType == null || importType == ProductImport.ImportType.UN_KNOW) {
            return flattenResult(this.joinGroupImportWithProductImportRepository.getDebt(groupID, supplier));
        }
        return flattenResult(this.joinGroupImportWithProductImportRepository.getDebtOfType(groupID, importType, supplier));
    }
}
