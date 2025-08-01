package com.example.heroku.controller;

import com.example.heroku.model.Users;
import com.example.heroku.model.productprice.ProductPriceChange;
import com.example.heroku.model.statistics.WareHouseIncomeOutCome;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.carousel.IDContainer;
import com.example.heroku.request.permission.WrapPermissionAction;
import com.example.heroku.request.permission.WrapPermissionGroupWithPrincipalAction;
import com.example.heroku.request.warehouse.GroupImportWithItem;
import com.example.heroku.request.warehouse.SearchImportQuery;
import com.example.heroku.response.Format;
import com.example.heroku.services.GroupImport;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/productimport")
public class ProductImportController {

    @Autowired
    GroupImport groupImport;


    @PostMapping("/admin/create")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Format>> addProductImportInfo(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid GroupImportWithItem groupImportWithItem) {
        System.out.println("add or update product warehouse: " + groupImportWithItem.getType() + ", group: " + groupImportWithItem.getGroup_id());
        return WrapPermissionGroupWithPrincipalAction.<ResponseEntity<Format>>builder()
                .principal(principal)
                .subject(groupImportWithItem::getGroup_id)
                .monoAction(() -> groupImport.SaveGroupImport(groupImportWithItem))
                .build().toMono();
    }

    @PostMapping("/admin/getallworking")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<GroupImportWithItem> getAllWorking(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid SearchQuery query) {
        System.out.println("get warehouse allworking of group: " + query.getGroup_id());
        return WrapPermissionAction.<GroupImportWithItem>builder()
                .principal(principal)
                .query(query)
                .fluxAction(q -> groupImport.GetAllWorking(q))
                .build()
                .toFlux();
    }

    @PostMapping("/admin/getallworkingofproduct")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<GroupImportWithItem> GetAllWorkingOfProduct(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid SearchImportQuery query) {
        System.out.println("get warehouse allworking of product: " + query.getProduct_second_id() + ", group: " + query.getGroup_id());
        return WrapPermissionAction.<GroupImportWithItem>builder()
                .principal(principal)
                .query(SearchQuery.builder().group_id(query.getGroup_id()).build())
                .fluxAction(q -> groupImport.GetAllWorkingOfProduct(query))
                .build()
                .toFlux();
    }

    @PostMapping("/admin/getallworkingbytype")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<GroupImportWithItem> GetAllWorkingByType(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid SearchImportQuery query) {
        System.out.println("get warehouse allworking by type: " + query.getType() + ", group: " + query.getGroup_id());
        return WrapPermissionAction.<GroupImportWithItem>builder()
                .principal(principal)
                .query(SearchQuery.builder().group_id(query.getGroup_id()).build())
                .fluxAction(q -> groupImport.GetAllWorkingAndType(query))
                .build()
                .toFlux();
    }

    @PostMapping("/admin/getallworkingbytypeofproduct")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<GroupImportWithItem> GetAllWorkingByTypeOfProduct(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid SearchImportQuery query) {
        System.out.println("get warehouse allworking by type: " + query.getType() + ", product: " + query.getProduct_second_id() + ", group: " + query.getGroup_id());
        return WrapPermissionAction.<GroupImportWithItem>builder()
                .principal(principal)
                .query(SearchQuery.builder().group_id(query.getGroup_id()).build())
                .fluxAction(q -> groupImport.GetAllWorkingOfProductAndType(query))
                .build()
                .toFlux();
    }

    @PostMapping("/admin/getallworkingbetween")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<GroupImportWithItem> GetAllWorkingBetween(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid SearchImportQuery query) {
        System.out.println("get warehouse allworking between from: " + query.getFrom() + ", to: " + query.getTo() + ", group: " + query.getGroup_id());
        return WrapPermissionAction.<GroupImportWithItem>builder()
                .principal(principal)
                .query(SearchQuery.builder().group_id(query.getGroup_id()).build())
                .fluxAction(q -> groupImport.GetAllWorkingBetween(query))
                .build()
                .toFlux();
    }

    @PostMapping("/admin/getallworkingbetweenbytype")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<GroupImportWithItem> GetAllWorkingBetweenByType(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid SearchImportQuery query) {
        System.out.println("get warehouse allworking between from: " + query.getFrom() + ", to: " + query.getTo() + ", type: " + query.getType() + ", group: " + query.getGroup_id());
        return WrapPermissionAction.<GroupImportWithItem>builder()
                .principal(principal)
                .query(SearchQuery.builder().group_id(query.getGroup_id()).build())
                .fluxAction(q -> groupImport.GetAllWorkingBetweenAndType(query))
                .build()
                .toFlux();
    }

    @PostMapping("/admin/getallworkingbetweenbytypeofproduct")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<GroupImportWithItem> GetAllWorkingBetweenByTypeOfProduct(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid SearchImportQuery query) {
        System.out.println("get warehouse allworking between from: " + query.getFrom() + ", to: " + query.getTo() + ", type: " + query.getType() + ", product: " + query.getProduct_second_id() + ", group: " + query.getGroup_id());
        return WrapPermissionAction.<GroupImportWithItem>builder()
                .principal(principal)
                .query(SearchQuery.builder().group_id(query.getGroup_id()).build())
                .fluxAction(q -> groupImport.GetAllWorkingOfProductBetweenAndType(query))
                .build()
                .toFlux();
    }

    @PostMapping("/admin/getallworkingbetweenofproduct")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<GroupImportWithItem> GetAllWorkingBetweenOfProduct(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid SearchImportQuery query) {
        System.out.println("get warehouse allworking between from: " + query.getFrom() + ", to: " + query.getTo() + ", product: " + query.getProduct_second_id() + ", group: " + query.getGroup_id());
        return WrapPermissionAction.<GroupImportWithItem>builder()
                .principal(principal)
                .query(SearchQuery.builder().group_id(query.getGroup_id()).build())
                .fluxAction(q -> groupImport.GetAllWorkingOfProductBetween(query))
                .build()
                .toFlux();
    }

    @PostMapping("/admin/getbygroupimportid")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<GroupImportWithItem> GetByGroupImportID(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid SearchImportQuery query) {
        System.out.println("get by group import ID: " + query.getGroup_id() + ", ID: " + query.getGroup_import_second_id());
        return WrapPermissionAction.<GroupImportWithItem>builder()
                .principal(principal)
                .query(SearchQuery.builder().group_id(query.getGroup_id()).build())
                .fluxAction(q -> groupImport.GetByGroupImportID(query))
                .build()
                .toFlux();
    }

    @PostMapping("/admin/getstatisticwarehouse")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<WareHouseIncomeOutCome> GetStaticsicWarehouse(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid SearchImportQuery query) {
        System.out.println("get statictis by group import ID: " + query.getGroup_id() + ", from: " + query.getFrom());
        return WrapPermissionAction.<WareHouseIncomeOutCome>builder()
                .principal(principal)
                .query(SearchQuery.builder().group_id(query.getGroup_id()).build())
                .monoAction(q -> groupImport.GetWareHouseStatictisBetween(query))
                .build()
                .toMono();
    }


    @PostMapping("/admin/return")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Format>> Return(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid IDContainer idContainer) {
        System.out.println("return : " + idContainer.getId() + ", group: " + idContainer.getGroup_id());
        return WrapPermissionAction.<ResponseEntity<Format>>builder()
                .principal(principal)
                .query(SearchQuery.builder().group_id(idContainer.getGroup_id()).build())
                .monoAction(q -> groupImport.Return(idContainer))
                .build()
                .toMono();
    }

    @PostMapping("/admin/searchtype")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<GroupImportWithItem> SearchType(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid SearchImportQuery query) {
        System.out.println("Search by group import ID: " + query.getGroup_id() + ", type: " + query.getType());
        return WrapPermissionAction.<GroupImportWithItem>builder()
                .principal(principal)
                .query(SearchQuery.builder().group_id(query.getGroup_id()).build())
                .fluxAction(q -> groupImport.SearchByType(query))
                .build()
                .toFlux();
    }

    @PostMapping("/admin/search")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<GroupImportWithItem> Search(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid SearchImportQuery query) {
        System.out.println("Search by group import ID: " + query.getGroup_id());
        return WrapPermissionAction.<GroupImportWithItem>builder()
                .principal(principal)
                .query(SearchQuery.builder().group_id(query.getGroup_id()).build())
                .fluxAction(q -> groupImport.Search(query))
                .build()
                .toFlux();
    }

    @PostMapping("/admin/getpricechange")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<ProductPriceChange> GetProductImportPriceChange(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid SearchImportQuery query) {
        System.out.println("get product import price change between from: " + query.getFrom() + ", to: " + query.getTo() + ", group: " + query.getGroup_id());
        return WrapPermissionAction.<ProductPriceChange>builder()
                .principal(principal)
                .query(SearchQuery.builder().group_id(query.getGroup_id()).build())
                .fluxAction(q -> groupImport.GetPriceRange(query))
                .build()
                .toFlux();
    }
}
