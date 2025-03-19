package com.example.heroku.controller.root;

import com.example.heroku.jwt.JwtTokenProvider;
import com.example.heroku.model.Tokens;
import com.example.heroku.model.repository.UserRepository;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.carousel.IDContainer;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/root/token")
public class RootTokenController {

    @Autowired
    private com.example.heroku.services.Tokens tokens;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository userRepository;


    @PostMapping("/create")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<Tokens> createTokenWithoutPermission(@RequestBody @Valid IDContainer idContainer) {
        return userRepository.findByUsername(idContainer.getId())
                .map(u -> {
                            u.setPassword(u.getPassword().trim());
                            return u.parseauthorities();
                        }
                )
                .map(this.jwtTokenProvider::createToken)
                .flatMap(s -> tokens.createToken(idContainer.getGroup_id(), Util.getInstance().GenerateIDOrg(), s, idContainer.getId(), 0));
    }

    @PostMapping("/getall")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<Tokens> getAllToken(@RequestBody @Valid SearchQuery query) {
        return tokens.getAllToken(query);
    }
}
