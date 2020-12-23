package com.example.heroku.controller;

import com.example.heroku.model.Image;
import com.example.heroku.model.repository.ImageRepository;
import com.example.heroku.response.Format;
import com.example.heroku.util.Util;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.Date;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/carousel")
public class CarouselController {

    @Autowired
    ImageRepository imageRepository;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public Flux<Object> uploadFile(@RequestPart("file") Flux<FilePart> file) {
        String imageID = Util.getInstance().GenerateID();
        System.out.println("Upload image: "+imageID);
        return file.flatMap(f -> f.content().map(it -> {
                    try {
                        return IOUtils.toByteArray(it.asInputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
        ).flatMap(content -> this.imageRepository.save(Image.builder()
                .content(content)
                .category("Carousel")
                .createat(new Date())
                .build())
        ).map(save -> ok(Format.builder().response(save.getId()).build()));
    }

    @PostMapping("/delete")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Object> uploadFile(@RequestParam("img") String imageID) {
        String message = "";
        try {
            this.imageRepository.deleteById(imageID);
            return ResponseEntity.ok().body(Format.builder().response(imageID).build());
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            message = "Could not delete the file: " + imageID + "!";
            return  ResponseEntity.badRequest().body(message);
        }
    }

    @GetMapping("/all")
    @CrossOrigin(origins = "http://localhost:4200")
    public Flux<Image> getAll(){
        return this.imageRepository.findByCategory("Carousel")
                .switchIfEmpty(Flux.empty());
    }
}