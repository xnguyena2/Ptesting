package com.example.heroku.controller;

import com.example.heroku.model.Voucher;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.voucher.VoucherData;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/voucher")
public class VoucherController {
    @Autowired
    com.example.heroku.services.Voucher voucherAPI;


    @PostMapping("/admin/getall")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<VoucherData> getAllVoucher(@RequestBody @Valid SearchQuery query) {
        System.out.println("Get all voucher: page " + query.getPage() + ", size " + query.getSize());
        return voucherAPI.getAllVoucher(query.getPage(), query.getSize());
    }

    @GetMapping("/admin/detail/{id}")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<VoucherData> getDetail(@PathVariable("id") String id) {
        System.out.println("Get voucher: " + id);
        return voucherAPI.getVoucherByID(id);
    }

    @PostMapping("/admin/create")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<Voucher> create(@RequestBody @Valid VoucherData voucherData) {
        System.out.println("create new voucher: "+voucherData.getVoucher_second_id());
        return voucherAPI.createVoucher(voucherData);
    }

    @PostMapping("/admin/delete")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<Voucher> delete(@RequestBody @Valid VoucherData voucherData) {
        System.out.println("delete Voucher: " + voucherData.getVoucher_second_id());
        return voucherAPI.deleteByID(voucherData.getVoucher_second_id());
    }
}
