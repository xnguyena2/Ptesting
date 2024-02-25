package com.example.heroku.controller;

import com.example.heroku.model.Image;
import com.example.heroku.model.Users;
import com.example.heroku.request.carousel.IDContainer;
import com.example.heroku.request.permission.WrapPermissionGroupWithPrincipalAction;
import com.example.heroku.response.Format;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/carousel")
public class CarouselController {

    @Autowired
    com.example.heroku.services.Image imageAPI;

    @PostMapping(value = "/admin/{groupid}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_NDJSON_VALUE)
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<com.example.heroku.model.Image>> uploadFile(@AuthenticationPrincipal Mono<Users> principal, @RequestPart("file") Flux<FilePart> file, @PathVariable("groupid") String groupid) {
        System.out.println("Upload carousel image");
        return WrapPermissionGroupWithPrincipalAction.<ResponseEntity<com.example.heroku.model.Image>>builder()
                .principal(principal)
                .subject(() -> groupid)
                .monoAction(() -> imageAPI.Upload(file, "Carousel", groupid))
                .build().toMono();
    }

    @PostMapping("/admin/delete")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Format>> uploadFile(@AuthenticationPrincipal Mono<Users> principal, @Valid @ModelAttribute IDContainer img) {
        System.out.println("Delete img: " + img.getId());
        return WrapPermissionGroupWithPrincipalAction.<ResponseEntity<Format>>builder()
                .principal(principal)
                .subject(img::getGroup_id)
                .monoAction(() -> imageAPI.Delete(img))
                .build().toMono();
    }

    @GetMapping("/admin/{groupid}/all")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<Image> getAll(@AuthenticationPrincipal Mono<Users> principal, @PathVariable("groupid") String groupid){
        return WrapPermissionGroupWithPrincipalAction.<Image>builder()
                .principal(principal)
                .subject(() -> groupid)
                .fluxAction(() -> imageAPI.GetAll(groupid, "Carousel"))
                .build().toFlux();
    }
}