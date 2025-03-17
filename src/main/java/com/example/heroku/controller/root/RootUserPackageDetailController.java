package com.example.heroku.controller.root;

import com.example.heroku.jwt.JwtTokenProvider;
import com.example.heroku.model.Tokens;
import com.example.heroku.model.repository.UserRepository;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.carousel.IDContainer;
import com.example.heroku.response.PackageDataResponse;
import com.example.heroku.services.UserPackage;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/root/userpackagedetail")
public class RootUserPackageDetailController {

    @Autowired
    UserPackage userRepository;

    @PostMapping("/getpackageofnotpaid")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<PackageDataResponse> getAllToken(@RequestBody @Valid SearchQuery query) {
        return userRepository.GetDonePackageOfStorWithPaymentStatus("NOT_PAID", "%" + query.getQuery() + "%");
    }
}
