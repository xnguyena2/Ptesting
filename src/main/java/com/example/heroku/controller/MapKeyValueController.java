package com.example.heroku.controller;

import com.example.heroku.model.Users;
import com.example.heroku.request.permission.WrapPermissionGroupWithPrincipalAction;
import com.example.heroku.services.MapKeyValue;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/keyvalue")
public class MapKeyValueController {

    @Autowired
    private MapKeyValue mapKeyValue;

    @PostMapping("/admin/insert")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<com.example.heroku.model.MapKeyValue> insert(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid com.example.heroku.model.MapKeyValue obj) {

        System.out.println("group: " + obj.getGroup_id() + ", insert key: " + obj.getId_o() + ", value: " + obj.getValue_o());
        return WrapPermissionGroupWithPrincipalAction.<com.example.heroku.model.MapKeyValue>builder()
                .principal(principal)
                .subject(obj::getGroup_id)
                .monoAction(() -> mapKeyValue.insert(obj))
                .build().toMono();
    }

    @GetMapping("/get/{groupid}/{id}")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<com.example.heroku.model.MapKeyValue> get(@AuthenticationPrincipal Mono<Users> principal, @PathVariable("groupid") String groupid, @PathVariable("id") String id) {

        System.out.println("group: " + groupid + ", get key: " + id);
        return WrapPermissionGroupWithPrincipalAction.<com.example.heroku.model.MapKeyValue>builder()
                .principal(principal)
                .subject(() -> groupid)
                .monoAction(() -> mapKeyValue.getByID(groupid, id))
                .build().toMono();
    }

    @GetMapping("/search/{groupid}/{id}")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<com.example.heroku.model.MapKeyValue> search(@AuthenticationPrincipal Mono<Users> principal, @PathVariable("groupid") String groupid, @PathVariable("id") String id) {

        System.out.println("group: " + groupid + ", search: " + id);
        return WrapPermissionGroupWithPrincipalAction.<com.example.heroku.model.MapKeyValue>builder()
                .principal(principal)
                .subject(() -> groupid)
                .fluxAction(() -> mapKeyValue.search(groupid, id))
                .build().toFlux();
    }

    @PostMapping("/admin/delete")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<com.example.heroku.model.MapKeyValue> delete(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid com.example.heroku.model.MapKeyValue obj) {

        System.out.println("group: " + obj.getGroup_id() + ", delete key: " + obj.getId_o());
        return WrapPermissionGroupWithPrincipalAction.<com.example.heroku.model.MapKeyValue>builder()
                .principal(principal)
                .subject(obj::getGroup_id)
                .monoAction(() -> mapKeyValue.deleteByID(obj.getGroup_id(), obj.getId_o()))
                .build().toMono();
    }
}
