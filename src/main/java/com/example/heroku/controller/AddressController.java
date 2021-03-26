package com.example.heroku.controller;

import com.example.heroku.services.VietNamAddress;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.io.IOException;

@RestController
@RequestMapping("/address")
public class AddressController {
    @Autowired
    VietNamAddress addressAPI;

    @GetMapping("/allregion")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<VietNamAddress.AddressFromat.Region> getAllRegion() throws IOException {
        return addressAPI.GetAllRegion();
    }

    @GetMapping("/district/{region}")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<VietNamAddress.AddressFromat.Region.DistrictContent.District> getDistrict(@PathVariable("region") int region) throws IOException {
        return  addressAPI.GetAllDistrict(region);
    }
}
