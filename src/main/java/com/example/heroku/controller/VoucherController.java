package com.example.heroku.controller;

import com.example.heroku.model.Voucher;
import com.example.heroku.request.page.Page;
import com.example.heroku.request.voucher.VoucherData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/voucher")
public class VoucherController {
    @Autowired
    com.example.heroku.services.Voucher voucherAPI;

    @GetMapping("/generateid")
    @CrossOrigin(origins = "http://localhost:4200")
    public Mono<ResponseEntity> generateID(){
        return voucherAPI.generateID();
    }

    @PostMapping("/getall")
    @CrossOrigin(origins = "http://localhost:4200")
    public Flux<Voucher> getAllVoucher(@RequestBody @Valid Page page) {
        System.out.println("Get all voucher: page " + page.getPage() + ", size " + page.getSize());
        return voucherAPI.getAllVoucher(page.getPage(), page.getSize());
    }

    @PostMapping("/create")
    @CrossOrigin(origins = "http://localhost:4200")
    public Mono<Voucher> deleteIMG(@RequestBody @Valid VoucherData voucherData) {
        System.out.println("create new voucher: "+voucherData.getVoucher().getVoucher_second_id());
        return voucherAPI.createVoucher(voucherData);
    }
}
