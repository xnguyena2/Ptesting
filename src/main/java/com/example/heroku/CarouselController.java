package com.example.heroku;

import com.example.heroku.model.Image;
import com.example.heroku.model.repository.ImageRepository;
import com.example.heroku.response.Format;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Optional;

@RestController
@RequestMapping("/carousel")
public class CarouselController {

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    private DataSource dataSource;

    @GetMapping(
            value = "/img/{id}",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public @ResponseBody
    byte[] getImg(@PathVariable String id) throws UnsupportedEncodingException {
        System.out.println("get img: "+id);
        Optional<Image> result = imageRepository.findById(id);
        if(result.isPresent()){
            return Base64.getDecoder().decode(result.get().getContent().getBytes("UTF-8"));
        }
        return null;
    }

    @PostMapping("/upload")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {

            String imageID = Util.getInstance().GenerateID();
            String encoded = Base64.getEncoder().encodeToString(file.getBytes());

            NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
            mapSqlParameterSource.addValue("content", encoded);
            mapSqlParameterSource.addValue("id", imageID);
            mapSqlParameterSource.addValue("category", "Carousel");
            jdbcTemplate.update("INSERT INTO IMAGE (ImgID, Content, Category, CreateAt) VALUES (:id, :content, :category, now())",mapSqlParameterSource);

            return ResponseEntity.ok().body(Format.builder().response(imageID).build());
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return  ResponseEntity.badRequest().body(message);
        }
    }
}