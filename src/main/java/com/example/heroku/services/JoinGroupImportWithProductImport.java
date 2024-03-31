package com.example.heroku.services;

import com.example.heroku.model.ProductImport;
import com.example.heroku.model.joinwith.GroupImportJoinProductImport;
import com.example.heroku.model.repository.JoinGroupImportWithProductImportRepository;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.warehouse.GroupImportWithItem;
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

    public Flux<GroupImportWithItem> GetAllGroupImport(SearchQuery query) {
        return flattenResult(this.joinGroupImportWithProductImportRepository.getAllProduct(query.getGroup_id(), query.getPage(), query.getSize()));
    }
}
