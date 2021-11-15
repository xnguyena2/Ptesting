package com.example.heroku.controller;

import com.example.heroku.model.Image;
import com.example.heroku.request.carousel.IDContainer;
import com.example.heroku.response.Format;
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
@RequestMapping("/image")
public class ImageController {

    @Autowired
    com.example.heroku.services.Image imageAPI;

    //---------- for manage image------------------
    @PostMapping(value = "/admin/{id}/img/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Image>> uploadIMG(@RequestPart("file") Flux<FilePart> file, @PathVariable("id") String categoryID) {
        System.out.println("Uplaod image for product!");
        return imageAPI.Upload(file, categoryID);
    }

    @PostMapping("/admin/{id}/img/delete")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Format>> deleteIMG(@Valid @ModelAttribute IDContainer img) {
        System.out.println("delete imgae: " + img.getId());
        return imageAPI.Delete(img);
    }

    @GetMapping("/admin/{id}/img/all")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<Image> getIMGbyID(@PathVariable("id") String categoryID) {
        return imageAPI.GetAll(categoryID);
    }

    //--------------------end manage image-----------------

}
