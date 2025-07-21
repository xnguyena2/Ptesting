package com.example.heroku.controller;

import com.example.heroku.model.Image;
import com.example.heroku.model.Product;
import com.example.heroku.model.Users;
import com.example.heroku.model.joinwith.ComboItemJoinProductUnitAndProduct;
import com.example.heroku.request.beer.*;
import com.example.heroku.request.carousel.IDContainer;
import com.example.heroku.request.permission.WrapPermissionAction;
import com.example.heroku.request.permission.WrapPermissionGroupWithPrincipalAction;
import com.example.heroku.response.Format;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/beer")
public class BeerController {

    @Autowired
    com.example.heroku.services.Image imageAPI;

    @Autowired
    com.example.heroku.services.Beer beerAPI;


    //---------- for manage image------------------
    @PostMapping(value = "/admin/{groupid}/{id}/img/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_NDJSON_VALUE)
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<com.example.heroku.model.Image>> uploadIMG(@AuthenticationPrincipal Mono<Users> principal, @RequestPart("file") Flux<FilePart> file, @PathVariable("groupid") String groupid, @PathVariable("id") String beerID) {
        System.out.println("Uplaod image for beer! of group: " + groupid + ", id: " + beerID);
        return WrapPermissionGroupWithPrincipalAction.<ResponseEntity<com.example.heroku.model.Image>>builder()
                .principal(principal)
                .subject(() -> groupid)
                .monoAction(() -> imageAPI.Upload(file, beerID, groupid, null))
                .build().toMono();
    }
    @PostMapping(value = "/admin/{groupid}/{id}/{tag}/img/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_NDJSON_VALUE)
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<com.example.heroku.model.Image>> uploadIMGWithTag(@AuthenticationPrincipal Mono<Users> principal, @RequestPart("file") Flux<FilePart> file,
                                                                                 @PathVariable("groupid") String groupid, @PathVariable("id") String beerID, @PathVariable("tag") String tag) {
        System.out.println("Uplaod image for beer! of group: " + groupid + ", id: " + beerID + ", tag: " + tag);
        return WrapPermissionGroupWithPrincipalAction.<ResponseEntity<com.example.heroku.model.Image>>builder()
                .principal(principal)
                .subject(() -> groupid)
                .monoAction(() -> imageAPI.Upload(file, beerID, groupid, tag))
                .build().toMono();
    }

    @PostMapping("/admin/img/delete")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Format>> deleteIMG(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid IDContainer img) {
        System.out.println("delete imgae: " + img.getId());
        return WrapPermissionGroupWithPrincipalAction.<ResponseEntity<Format>>builder()
                .principal(principal)
                .subject(img::getGroup_id)
                .monoAction(() -> imageAPI.Delete(img))
                .build().toMono();
    }

    @GetMapping("/admin/{groupid}/{id}/img/all")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<Image> getIMGbyID(@AuthenticationPrincipal Mono<Users> principal, @PathVariable("groupid") String groupid, @PathVariable("id") String beerID) {
        System.out.println("get image: " + beerID + ", groupid: " + groupid);
        return WrapPermissionGroupWithPrincipalAction.<Image>builder()
                .principal(principal)
                .subject(() -> groupid)
                .fluxAction(() -> imageAPI.GetAll(groupid, beerID))
                .build().toFlux();
    }

    //--------------------end manage image-----------------


    @PostMapping("/admin/create")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<BeerSubmitData>> addBeerInfo(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid BeerSubmitData beerInfo) {
        BeerInfo beerInf = beerInfo.GetBeerInfo();
        System.out.println("group: " + beerInf.getProduct().getGroup_id() + ", add or update beer: " + beerInf.getProduct().getName());
        return WrapPermissionGroupWithPrincipalAction.<ResponseEntity<BeerSubmitData>>builder()
                .principal(principal)
                .subject(beerInfo::getGroup_id)
                .monoAction(() -> beerAPI.CreateBeer(beerInf)
                        .map(ResponseEntity::ok)
                        .onErrorResume(throwable -> {
                                    String msg = throwable.getMessage();
                                    System.out.println(msg);
                                    if (!msg.contains("uq_product_unit_upc") && !msg.contains("uq_product_unit_sku") && !msg.contains("uq_product_serial_id")) {
                                        throw new RuntimeException(throwable);
                                    }
                                    beerInfo.setName(msg);
                                    return Mono.just(org.springframework.http.ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                            .body(beerInfo));
                                }
                        )
                )
                .build().toMono();
    }

    @PostMapping("/admin/getcomboitem")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<ComboItemJoinProductUnitAndProduct> getComboItem(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid IDContainer idContainer) {
        System.out.println("get combo item of beer: " + idContainer.getId());
        return WrapPermissionGroupWithPrincipalAction.<ComboItemJoinProductUnitAndProduct>builder()
                .principal(principal)
                .subject(idContainer::getGroup_id)
                .fluxAction(() -> beerAPI.GetAllCompoItem(idContainer))
                .build().toFlux();
    }

    @PostMapping("/admin/updateinventoryproductunit")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<String> updateInventoryProductUnit(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid ProductUnitUpdate productUnitUpdate) {
        System.out.println("update beer unit: " + productUnitUpdate.getProduct_unit_second_id() + ", enable_warehouse: " + productUnitUpdate.isEnable_warehouse() + ", inventory: " + productUnitUpdate.getInventory_number() + ", group_id: " + productUnitUpdate.getGroup_id());
        return WrapPermissionGroupWithPrincipalAction.<String>builder()
                .principal(principal)
                .subject(productUnitUpdate::getGroup_id)
                .monoAction(() -> beerAPI.UpdateProductUnitWareHouseSetting(productUnitUpdate))
                .build().toMono();
    }

    @DeleteMapping("/admin/delete/{groupid}/{id}")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<Product> delete(@AuthenticationPrincipal Mono<Users> principal, @PathVariable("groupid") String groupID, @PathVariable("id") String beerID) {
        System.out.println("delete beer: " + beerID + ", group_id: " + groupID);
        return WrapPermissionGroupWithPrincipalAction.<Product>builder()
                .principal(principal)
                .subject(() -> groupID)
                .monoAction(() -> beerAPI.DeleteBeerByID(groupID, beerID))
                .build().toMono();
    }



    @PostMapping("/getall")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<SearchResult<BeerSubmitData>> getAll(@RequestBody @Valid SearchQuery query) {
        System.out.println("get all beer: " + query.getPage());
        return beerAPI.CountGetAllBeer(query);
    }

    @PostMapping("/search")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<SearchResult<BeerSubmitData>> search(@RequestBody @Valid SearchQuery query) {
        System.out.println("Search beer: " + query.getQuery());
        return beerAPI.CountSearchBeer(query);
    }

    @PostMapping("/category")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<SearchResult<BeerSubmitData>> category(@RequestBody @Valid SearchQuery query) {
        System.out.println("category beer: " + query.getQuery());
        return beerAPI.CountGetAllBeerByCategory(query);
    }

    @GetMapping("/detail/{groupid}/{id}")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<BeerSubmitData> detail(@PathVariable("groupid") String groupID, @PathVariable("id") String beerID) {
        System.out.println("Detail of beer: " + beerID);
        return beerAPI.GetBeerByID(groupID, beerID);
    }
}
