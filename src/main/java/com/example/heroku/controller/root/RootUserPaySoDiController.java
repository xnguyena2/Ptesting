package com.example.heroku.controller.root;

import com.example.heroku.model.UserPaySoDi;
import com.example.heroku.request.client.UserID;
import com.example.heroku.response.Format;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/root/paysodi")
public class RootUserPaySoDiController {

    @Autowired
    private com.example.heroku.services.UserPaySoDi userPaySoDi;

    @PostMapping("/addpayment")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Format>> addPayment(@RequestBody @Valid UserPaySoDi paySoDi) {
        return userPaySoDi.createTransaction(paySoDi);
    }

    @GetMapping("/group/{groupID}")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<UserPaySoDi> findByGroupID(@PathVariable("groupID") String groupID) {
        return userPaySoDi.findByGroupId(groupID);
    }

    @PostMapping("/deletebyid/")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<UserPaySoDi> deleteByID(@RequestBody @Valid UserID id) {
        return userPaySoDi.deleteByID(id.getGroup_id(), id.getAfter_id());
    }
}
