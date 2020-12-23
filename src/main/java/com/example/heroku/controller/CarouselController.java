package com.example.heroku.controller;

import com.example.heroku.error.EmptyException;
import com.example.heroku.model.Image;
import com.example.heroku.model.repository.ImageRepository;
import com.example.heroku.response.Format;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/carousel")
public class CarouselController {

    @Autowired
    ImageRepository imageRepository;

    @PostMapping("/upload")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            String imageID = Util.getInstance().GenerateID();

            this.imageRepository.save(Image.builder()
                    .content(file.getBytes())
                    .category("Carousel")
                    .createAt(new Date())
                    .build()
            );
            return ResponseEntity.ok().body(Format.builder().response(imageID).build());
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return  ResponseEntity.badRequest().body(message);
        }
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
    @Transactional(readOnly = true)
    @CrossOrigin(origins = "http://localhost:4200")
    public Mono<String> getAll(){
        return this.imageRepository.getImgID("Carousel")
                .switchIfEmpty(Mono.error(new EmptyException()))
                .map(String::trim);
    }
}