package com.example.heroku.controller;

import com.example.heroku.model.UserAddress;
import com.example.heroku.request.address.AddressData;
import com.example.heroku.response.Format;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/deviceaddress")
public class UserAddressController {
    @Autowired
    com.example.heroku.services.UserAddress userAddress;

    @PostMapping("/create")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Format>> createAddress(@RequestBody @Valid AddressData addressInfo) {
        System.out.println("Create new address");
        return userAddress.CreateAddress(addressInfo.CovertModel(false));
    }

    @GetMapping("/all/{deviceid}")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<AddressData> getAllAddress(@PathVariable("deviceid") String deviceID) throws IOException {
        return userAddress.GetUserAddress(deviceID).flatMap(AddressData::FromModel);
    }

    @PostMapping("/delete")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<UserAddress> deleteAddress(@RequestBody @Valid AddressData addressInfo) {
        System.out.println("delete address");
        return userAddress.DeleteAddress(addressInfo.getAddress_id());
    }

    @PostMapping("/update")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<UserAddress> updateAddress(@RequestBody @Valid AddressData addressInfo) {
        System.out.println("update address");
        return userAddress.UpdateAddress(addressInfo.CovertModel(true));
    }
}
