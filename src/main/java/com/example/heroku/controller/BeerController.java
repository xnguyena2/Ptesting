package com.example.heroku.controller;

import com.example.heroku.model.Image;
import com.example.heroku.model.repository.ImageRepository;
import com.example.heroku.response.Format;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/beer")
public class BeerController {

    @Autowired
    ImageRepository imageRepository;

    @GetMapping("/generateid")
    @Transactional(readOnly = true)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity generateID(){
        return ok(Format.builder().response(Util.getInstance().GenerateID()).build());
    }

    @PostMapping("/{id}/img/upload")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Object> uploadIMG(@RequestParam("file") MultipartFile file, @PathVariable("id") String beerID) {
        String message = "";
        try {
            String imageID = Util.getInstance().GenerateID();

            this.imageRepository.save(Image.builder()
                    .ImgID(imageID)
                    .content(file.getBytes())
                    .category(beerID)
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

    @PostMapping("/{id}/img/delete")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Object> deleteIMG(@RequestParam("img") String imageID) {
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

    @GetMapping("/{id}/img/all")
    @Transactional(readOnly = true)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity getIMGbyID(@PathVariable("id") String beerID){
        List<String> allCarousel = this.imageRepository.getImgID(beerID).map(String::trim).collect(Collectors.toList());
        return ok(allCarousel);
    }
}
