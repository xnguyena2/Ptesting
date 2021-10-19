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
@RequestMapping("/carousel")
public class CarouselController {

    @Autowired
    com.example.heroku.services.Image imageAPI;

    @PostMapping(value = "/admin/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<com.example.heroku.model.Image>> uploadFile(@RequestPart("file") Flux<FilePart> file) {
        System.out.println("Upload carousel image");
        return imageAPI.Upload(file,"Carousel");
    }

    @PostMapping("/admin/delete")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Format>> uploadFile(@Valid @ModelAttribute IDContainer img) {
        System.out.println("Delete img: " + img.getId());
        return imageAPI.Delete(img);
    }

    @GetMapping("/admin/all")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<Image> getAll(){
        return imageAPI.GetAll("Carousel");
    }
}