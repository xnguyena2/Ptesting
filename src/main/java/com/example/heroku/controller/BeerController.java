package com.example.heroku.controller;

import com.example.heroku.model.Beer;
import com.example.heroku.model.BeerUnit;
import com.example.heroku.model.Image;
import com.example.heroku.request.beer.BeerSubmitData;
import com.example.heroku.request.carousel.IDContainer;
import com.example.heroku.request.page.Page;
import com.example.heroku.response.Format;
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

    @GetMapping("/generateid")
    @CrossOrigin(origins = "http://localhost:4200")
    public Mono<ResponseEntity> generateID(){
        return beerAPI.generateID();
    }


    //---------- for manage image------------------
    @PostMapping(value = "/{id}/img/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public Mono<ResponseEntity<Format>> uploadIMG(@RequestPart("file") Flux<FilePart> file, @PathVariable("id") String beerID) {
        System.out.println("Uplaod image for beer!");
        return imageAPI.Upload(file, beerID);
    }

    @PostMapping("/{id}/img/delete")
    @CrossOrigin(origins = "http://localhost:4200")
    public Mono<Object> deleteIMG(@Valid @ModelAttribute IDContainer img) {
        System.out.println("delete imgae: "+img.getId());
        return imageAPI.Delete(img);
    }

    @GetMapping("/{id}/img/all")
    @CrossOrigin(origins = "http://localhost:4200")
    public Flux<Image> getIMGbyID(@PathVariable("id") String beerID){
        return imageAPI.GetAll(beerID);
    }
    //--------------------end manage image-----------------




    @PostMapping("/create")
    @CrossOrigin(origins = "http://localhost:4200")
    public Flux<BeerUnit> addBeerInfo(@RequestBody @Valid BeerSubmitData beerInfo) {
        System.out.println("add or update beer: "+beerInfo.GetBeerInfo().getBeer().getName());
        return beerAPI.CreateBeer(beerInfo.GetBeerInfo());
    }

    @PostMapping("/getall")
    @CrossOrigin(origins = "http://localhost:4200")
    public Flux<BeerSubmitData> getAll(@RequestBody @Valid Page page) {
        System.out.println("get all beer: "+page.getPage());
        return beerAPI.GetAllBeer(page.getPage(), page.getSize());
    }

}
