package com.example.heroku.services;

import com.example.heroku.model.TableDetail;
import com.example.heroku.model.UserPackageDetail;
import com.example.heroku.model.joinwith.AreJoinTable;
import com.example.heroku.model.repository.AreaRepository;
import com.example.heroku.model.repository.TableDetailRepository;
import com.example.heroku.model.repository.UserPackageDetailRepository;
import com.example.heroku.model.repository.join.AreJoinTableAndPackageDetailRepository;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.client.AreaID;
import com.example.heroku.request.client.UserID;
import com.example.heroku.response.AreaData;
import com.example.heroku.response.TableDetailData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class Area {

    @Autowired
    UserPackageDetailRepository userPackageDetailRepository;

    @Autowired
    AreaRepository areaRepository;

    @Autowired
    AreJoinTableAndPackageDetailRepository areJoinTableAndPackageDetailRepository;

    @Autowired
    TableDetailRepository tableDetailRepository;


    Mono<AreaData> fillData(com.example.heroku.model.Area area) {
        return Mono.just(new AreaData(area))
                .flatMap(areaData ->
                        tableDetailRepository.findByAreaId(areaData.getGroup_id(), areaData.getArea_id())
                                .map(TableDetailData::new)
                                .map(areaData::AddTable)
                                .filter(tableDetailData -> tableDetailData.getPackage_second_id() != null)
                                .flatMap(tableDetailData -> userPackageDetailRepository.GetPackageDetailByIdOfStatus(tableDetailData.getGroup_id(), tableDetailData.getPackage_second_id(), tableDetailData.getTable_id(), UserPackageDetail.Status.CREATE)
                                        .map(userPackageDetail -> tableDetailData.setPrice(userPackageDetail.getPrice()))
                                )
                                .then(Mono.just(areaData))
                );
    }

    Mono<TableDetailData> insertOrUpdateTableDetail(TableDetailData tableDetailData) {
        return tableDetailRepository.insertOrUpdate(tableDetailData.getGroup_id(), tableDetailData.getArea_id(), tableDetailData.getTable_id(), tableDetailData.getTable_name(),
                        tableDetailData.getPackage_second_id(), tableDetailData.getDetail(), tableDetailData.getStatus(), tableDetailData.getCreateat())
                .then(Mono.just(tableDetailData));
    }

    Mono<com.example.heroku.model.Area> insertOrUpdate(com.example.heroku.model.Area area) {
        return areaRepository.insertOrUpdate(area.getGroup_id(), area.getArea_id(), area.getArea_name(), area.getDetail(), area.getMeta_search(), area.getStatus(), area.getCreateat());
    }

    Flux<TableDetail> saveAllTableDetail(List<TableDetailData> tableDetailList) {
        return Flux.fromIterable(tableDetailList).flatMap(
                this::insertOrUpdateTableDetail
        );
    }

    @Deprecated()
    public Flux<AreaData> getAllOld(UserID userID) {
        return areaRepository.findByIdNotNull(userID.getGroup_id(), PageRequest.of(userID.getPage(), userID.getSize(), Sort.by(Sort.Direction.ASC, "area_name")))
                .flatMap(this::fillData);
    }

    public Flux<AreaData> getAll(UserID userID) {
        return areJoinTableAndPackageDetailRepository.findByGroupID(userID.getGroup_id(), UserPackageDetail.Status.CREATE)
                .groupBy(AreJoinTable::getArea_id)
                .flatMap(stringAreJoinTableGroupedFlux -> stringAreJoinTableGroupedFlux.collectList().map(AreJoinTable::GenerateAreaData));
    }

    public Flux<AreaData> findArea(SearchQuery searchQuery) {
        String searchPattern = "%" + searchQuery.getQuery() + "%";
        return areaRepository.findByName(searchQuery.getGroup_id(), searchPattern,
                        PageRequest.of(searchQuery.getPage(), searchQuery.getSize(), Sort.by(Sort.Direction.ASC, "area_name")))
                .map(AreaData::new);
    }

    public Mono<AreaData> justCreateOrUpdateArea(AreaData areaData) {
        areaData.AutoFill();
        return insertOrUpdate(areaData)
                .then(Mono.just(areaData));
    }


    @Deprecated()
    public Mono<AreaData> getAreaByIdOld(AreaID areaID) {
        return areaRepository.getById(areaID.getGroup_id(), areaID.getArea_id())
                .map(AreaData::new)
                .flatMap(this::fillData);
    }

    public Mono<AreaData> getAreaById(AreaID areaID) {
        return areJoinTableAndPackageDetailRepository.findByID(areaID.getGroup_id(), areaID.getArea_id(), UserPackageDetail.Status.CREATE)
                .collectList().map(AreJoinTable::GenerateAreaData)
                .filter(areaData -> areaData.getArea_id() != null);
    }

    public Mono<AreaData> deleteArea(AreaData areaData) {
        return areaRepository.deleteById(areaData.getGroup_id(), areaData.getArea_id())
                .then(tableDetailRepository.deleteByAreaId(areaData.getGroup_id(), areaData.getArea_id()))
                .then(Mono.just(areaData));
    }

    public Mono<AreaData> createTableDetail(AreaData areaData) {
        return Mono.just(areaData)
                .map(AreaData::AutoFill)
                .filter(areaData1 -> areaData1.getListTable() != null)
                .flatMapMany(areaData1 -> saveAllTableDetail(areaData1.getListTable()))
                .then(Mono.just(areaData));
    }

    public Mono<TableDetailData> createOrUpdateTableDetail(TableDetailData tableDetailData) {
        tableDetailData.AutoFill();
        return insertOrUpdateTableDetail(tableDetailData);
    }

    public Mono<TableDetailData> setPackageID(TableDetailData tableDetailData) {
        return tableDetailRepository.cleanPackageID(tableDetailData.getGroup_id(), tableDetailData.getPackage_second_id())
                .then(
                        tableDetailRepository.setPackageID(tableDetailData.getGroup_id(), tableDetailData.getTable_id(), tableDetailData.getPackage_second_id())
                )
                .then(Mono.just(new TableDetailData(tableDetailData)));
    }

    public Mono<TableDetailData> deleteTableDetail(TableDetailData tableDetailData) {
        return tableDetailRepository.deleteById(tableDetailData.getGroup_id(), tableDetailData.getTable_id())
                .then(Mono.just(tableDetailData));
    }
}
