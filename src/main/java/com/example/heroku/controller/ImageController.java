package com.example.heroku.controller;

import com.example.heroku.model.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    com.example.heroku.services.Image imageAPI;

}
