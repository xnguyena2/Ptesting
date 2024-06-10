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
@RequestMapping("/image")
public class ImageController {

    @Autowired
    com.example.heroku.services.Image imageAPI;

    //---------- for manage image------------------
    @PostMapping(value = "/admin/{groupid}/{id}/img/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_NDJSON_VALUE)
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Image>> uploadIMG(@AuthenticationPrincipal Mono<Users> principal, @RequestPart("file") Flux<FilePart> file, @PathVariable("id") String categoryID, @PathVariable("groupid") String groupid) {
        System.out.println("Uplaod image for product!");
        return WrapPermissionGroupWithPrincipalAction.<ResponseEntity<Image>>builder()
                .principal(principal)
                .subject(() -> groupid)
                .monoAction(() -> imageAPI.Upload(file, categoryID, groupid, null))
                .build().toMono();
    }

    @PostMapping("/admin/{id}/img/delete")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Format>> deleteIMG(@AuthenticationPrincipal Mono<Users> principal, @Valid @ModelAttribute IDContainer img) {
        System.out.println("delete imgae: " + img.getId());
        return WrapPermissionGroupWithPrincipalAction.<ResponseEntity<Format>>builder()
                .principal(principal)
                .subject(img::getGroup_id)
                .monoAction(() -> imageAPI.Delete(img))
                .build().toMono();
    }

    @GetMapping("/admin/{groupid}/{id}/img/all")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<Image> getIMGbyID(@AuthenticationPrincipal Mono<Users> principal, @PathVariable("groupid") String groupid, @PathVariable("id") String categoryID) {
        return WrapPermissionGroupWithPrincipalAction.<Image>builder()
                .principal(principal)
                .subject(() -> groupid)
                .fluxAction(() -> imageAPI.GetAll(groupid, categoryID))
                .build().toFlux();
    }

    @GetMapping("/{groupid}/{id}/img/all")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<Image> getIMGbyIDWihtoutPermission(@AuthenticationPrincipal Mono<Users> principal, @PathVariable("groupid") String groupid, @PathVariable("id") String categoryID) {
        return WrapPermissionGroupWithPrincipalAction.<Image>builder()
                .principal(principal)
                .subject(() -> groupid)
                .fluxAction(() -> imageAPI.GetAll(groupid, categoryID))
                .build().toFlux();
    }

    //--------------------end manage image-----------------

}
