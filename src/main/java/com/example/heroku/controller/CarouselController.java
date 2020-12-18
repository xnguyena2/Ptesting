package com.example.heroku.controller;

import com.example.heroku.model.Image;
import com.example.heroku.model.repository.ImageRepository;
import com.example.heroku.response.Format;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
                    .ImgID(imageID)
                    .content(file.getBytes())
                    .category("Carousel")
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
}