package com.example.heroku.controller;

import com.example.heroku.model.Image;
import com.example.heroku.model.repository.ImageRepository;
import com.example.heroku.photo.PhotoLib;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    ImageRepository imageRepository;

    @GetMapping(
            value = "/{id}",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public @ResponseBody
    byte[] getImg(@PathVariable String id) {
        System.out.println("get img: " + id);
        return PhotoLib.getInstance().downloadFile(id);
    }
}
