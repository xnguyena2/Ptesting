package com.example.heroku.controller;

import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.client.AreaID;
import com.example.heroku.request.client.UserID;
import com.example.heroku.request.permission.WrapPermissionAction;
import com.example.heroku.response.AreaData;
import com.example.heroku.response.BuyerData;
import com.example.heroku.response.TableDetailData;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/table")
public class TableControler {

    @Autowired
    com.example.heroku.services.Area area;

    @PostMapping("/getall")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<AreaData> getAll(@RequestBody @Valid UserID userID) {
        return area.getAll(userID);
    }

    @PostMapping("/findarea")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<AreaData> findArea(@RequestBody @Valid SearchQuery searchQuery) {
        return area.findArea(searchQuery);
    }

    @PostMapping("/savearea")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<AreaData> saveArea(@RequestBody @Valid AreaData areaData) {
        return area.justCreateOrUpdateArea(areaData);
    }

    @PostMapping("/getarea")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<AreaData> getArea(@RequestBody @Valid AreaID areaID) {
        return area.getAreaById(areaID);
    }

    @PostMapping("/deletearea")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<AreaData> deleteArea(@RequestBody @Valid AreaData areaData) {
        return area.deleteArea(areaData);
    }

    @PostMapping("/createlisttable")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<AreaData> createListTable(@RequestBody @Valid AreaData areaData) {
        return area.createTableDetail(areaData);
    }

    @PostMapping("/savetable")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<TableDetailData> createTable(@RequestBody @Valid TableDetailData tableDetailData) {
        return area.createOrUpdateTableDetail(tableDetailData);
    }

    @PostMapping("/setpackage")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<TableDetailData> setPackage(@RequestBody @Valid TableDetailData tableDetailData) {
        return area.setPackageID(tableDetailData);
    }

    @PostMapping("/deletetable")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<TableDetailData> deleteTable(@RequestBody @Valid TableDetailData tableDetailData) {
        return area.deleteTableDetail(tableDetailData);
    }

}
