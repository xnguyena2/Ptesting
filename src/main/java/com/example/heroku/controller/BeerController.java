package com.example.heroku.controller;

import com.example.heroku.model.Beer;
import com.example.heroku.model.BeerUnit;
import com.example.heroku.model.Image;
import com.example.heroku.request.beer.BeerInfo;
import com.example.heroku.request.beer.BeerSubmitData;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.beer.SearchResult;
import com.example.heroku.request.carousel.IDContainer;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
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

    @GetMapping("/admin/generateid")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity> generateID() {
        return beerAPI.generateID();
    }


    //---------- for manage image------------------
    @PostMapping(value = "/admin/{id}/img/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<com.example.heroku.model.Image>> uploadIMG(@RequestPart("file") Flux<FilePart> file, @PathVariable("id") String beerID) {
        System.out.println("Uplaod image for beer!");
        return imageAPI.Upload(file, beerID);
    }

    @PostMapping("/admin/{id}/img/delete")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<Object> deleteIMG(@Valid @ModelAttribute IDContainer img) {
        System.out.println("delete imgae: " + img.getId());
        return imageAPI.Delete(img);
    }

    @GetMapping("/admin/{id}/img/all")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<Image> getIMGbyID(@PathVariable("id") String beerID) {
        return imageAPI.GetAll(beerID);
    }

    //--------------------end manage image-----------------


    @PostMapping("/admin/create")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<BeerUnit> addBeerInfo(@RequestBody @Valid BeerSubmitData beerInfo) {
        BeerInfo beerInf = beerInfo.GetBeerInfo();
        System.out.println("add or update beer: " + beerInf.getBeer().getName());
        return beerAPI.CreateBeer(beerInf);
    }

    @DeleteMapping("/admin/delete/{id}")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<Beer> delete(@PathVariable("id") String beerID) {
        System.out.println("delete beer: " + beerID);
        return beerAPI.DeleteBeerByID(beerID);
    }

    @PostMapping("/admin/getall")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<SearchResult> adminGetAll(@RequestBody @Valid SearchQuery query) {
        System.out.println("admin get all beer: " + query.getPage());
        return beerAPI.CountGetAllBeer(query);
    }




    @PostMapping("/getall")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<SearchResult> getAll(@RequestBody @Valid SearchQuery query) {
        System.out.println("get all beer: " + query.getPage());
        return beerAPI.CountGetAllBeer(query);
    }

    @PostMapping("/search")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<SearchResult> search(@RequestBody @Valid SearchQuery query) {
        System.out.println("Search beer: " + query.getQuery());
        return beerAPI.CountSearchBeer(query);
    }

    @PostMapping("/category")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<SearchResult> category(@RequestBody @Valid SearchQuery query) {
        System.out.println("category beer: " + query.getQuery());
        return beerAPI.CountGetAllBeerByCategory(query);
    }

    @GetMapping("/detail/{id}")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<BeerSubmitData> detail(@PathVariable("id") String beerID) {
        System.out.println("Detail of beer: " + beerID);
        return beerAPI.GetBeerByID(beerID);
    }
}
