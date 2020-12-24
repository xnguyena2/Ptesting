package com.example.heroku.controller;

import com.example.heroku.model.Image;
import com.example.heroku.model.repository.ImageRepository;
import com.example.heroku.response.Format;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/beer")
public class BeerController {

    @Autowired
    ImageRepository imageRepository;

    @GetMapping("/generateid")
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
                    .content(file.getBytes())
                    .category(beerID)
                    .createat(java.time.LocalDateTime.now())
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
    public ResponseEntity<Object> deleteIMG(@RequestParam("img") int imageID) {
        String message = "";
        try {
            this.imageRepository.deleteById(imageID);
            return ResponseEntity.ok().body(Format.builder().response(imageID+"").build());
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            message = "Could not delete the file: " + imageID + "!";
            return  ResponseEntity.badRequest().body(message);
        }
    }

    @GetMapping("/{id}/img/all")
    @CrossOrigin(origins = "http://localhost:4200")
    public Flux<Object> getIMGbyID(@PathVariable("id") String beerID){
        return this.imageRepository.findByCategory(beerID).map(x->x.getId());
    }
}
