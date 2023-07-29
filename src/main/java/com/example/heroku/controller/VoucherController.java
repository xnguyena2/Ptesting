package com.example.heroku.controller;

import com.example.heroku.model.Users;
import com.example.heroku.model.Voucher;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.permission.WrapPermissionAction;
import com.example.heroku.request.permission.WrapPermissionGroupWithPrincipalAction;
import com.example.heroku.request.voucher.VoucherData;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public Flux<VoucherData> getAllVoucher(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid SearchQuery query) {
        System.out.println("Get all voucher: page " + query.getPage() + ", size " + query.getSize());
        return WrapPermissionAction.<VoucherData>builder()
                .principal(principal)
                .query(query)
                .fluxAction(q -> voucherAPI.getAllVoucher(q))
                .build()
                .toFlux();
    }

    @GetMapping("/admin/detail/{groupid}/{id}")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<VoucherData> getDetail(@AuthenticationPrincipal Mono<Users> principal, @PathVariable("groupid") String groupid, @PathVariable("id") String id) {
        System.out.println("Get voucher: " + id);
        return WrapPermissionGroupWithPrincipalAction.<VoucherData>builder()
                .principal(principal)
                .subject(() -> groupid)
                .monoAction(() -> voucherAPI.getVoucherByID(groupid, id))
                .build().toMono();
    }

    @PostMapping("/admin/create")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<Voucher> create(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid VoucherData voucherData) {
        System.out.println("create new voucher: " + voucherData.getVoucher_second_id());
        return WrapPermissionGroupWithPrincipalAction.<Voucher>builder()
                .principal(principal)
                .subject(voucherData::getGroup_id)
                .monoAction(() -> voucherAPI.createVoucher(voucherData))
                .build().toMono();
    }

    @PostMapping("/admin/delete")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<Voucher> delete(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid VoucherData voucherData) {
        System.out.println("delete Voucher: " + voucherData.getVoucher_second_id());
        return WrapPermissionGroupWithPrincipalAction.<Voucher>builder()
                .principal(principal)
                .subject(voucherData::getGroup_id)
                .monoAction(() -> voucherAPI.deleteByID(voucherData))
                .build().toMono();
    }
}
