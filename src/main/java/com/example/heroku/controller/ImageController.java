package com.example.heroku.controller;

import com.example.heroku.model.repository.ImageRepository;
import com.example.heroku.photo.PhotoLib;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    com.example.heroku.services.Image imageAPI;

    @GetMapping(
            value = "/{id}",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public @ResponseBody
    byte[] getImg(@PathVariable String id) {
        System.out.println("get img: " + id);
        return PhotoLib.getInstance().downloadFile(id);
    }

    @GetMapping(
            value = "/thumbnail/{id}",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public @ResponseBody
    byte[] getThumbnailImg(@PathVariable("id") String id) {
        System.out.println("get thumnail img: " + id);
        return imageAPI.CreateThumbnailIMG(PhotoLib.getInstance().downloadFile(id), 50);
    }

    @GetMapping(
            value = "/preview/{id}",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public @ResponseBody
    byte[] getPreviewImg(@PathVariable("id") String id) {
        System.out.println("get preview img: " + id);
        return imageAPI.CreateThumbnailIMG(PhotoLib.getInstance().downloadFile(id), 300);
    }
}
