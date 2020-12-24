package com.example.heroku.controller;

import com.example.heroku.model.Image;
import com.example.heroku.request.carousel.DeleteImg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public Mono<Object> uploadFile(@RequestPart("file") Flux<FilePart> file) {
        System.out.println("Upload carousel image");
        return imageAPI.Upload(file,"Carousel");
    }

    @PostMapping("/delete")
    @CrossOrigin(origins = "http://localhost:4200")
    public Mono<Object> uploadFile(@Valid @ModelAttribute DeleteImg img) {
        System.out.println("Delete img: " + img.getImg());
        return imageAPI.Delete(img);
    }

    @GetMapping("/all")
    @CrossOrigin(origins = "http://localhost:4200")
    public Flux<Image> getAll(){
        return imageAPI.GetAll("Carousel");
    }
}