package com.example.heroku.controller;

import com.example.heroku.model.Image;
import com.example.heroku.model.repository.ImageRepository;
import com.example.heroku.request.carousel.DeleteImg;
import com.example.heroku.response.Format;
import com.example.heroku.util.Util;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.io.IOException;
import java.io.SequenceInputStream;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/carousel")
public class CarouselController {

    @Autowired
    ImageRepository imageRepository;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public Mono<Object> uploadFile(@RequestPart("file") Flux<FilePart> file) {
        String imageID = Util.getInstance().GenerateID();
        System.out.println("Upload image: "+imageID);
        return file.flatMap(f -> f.content().map(it -> it.asInputStream())).reduce(SequenceInputStream::new)
                .flatMap(initialStream -> {
                    try {
                        byte[] content = IOUtils.toByteArray(initialStream);
                        return this.imageRepository.save(Image.builder()
                                .content(content)
                                .category("Carousel")
                                .createat(java.time.LocalDateTime.now())
                                .build());
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }).map(save -> ok(Format.builder().response(save.getId()).build()));
    }

    @PostMapping("/delete")
    @CrossOrigin(origins = "http://localhost:4200")
    public Mono<Object> uploadFile(@Valid @ModelAttribute DeleteImg img) {
        try {
            System.out.println("Delete img: "+img.getImg());
            return Mono.just(img).flatMap(info->this.imageRepository.deleteById(Integer.parseInt(info.getImg()))
                    .map(deleted -> ok(Format.builder().response(deleted.getId()).build())));
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            String message  = "Could not delete the file!";
            return Mono.just(badRequest().body(message));
        }
    }

    @GetMapping("/all")
    @CrossOrigin(origins = "http://localhost:4200")
    public Flux<Image> getAll(){
        return this.imageRepository.findByCategory("Carousel")
                .switchIfEmpty(Flux.empty());
    }
}