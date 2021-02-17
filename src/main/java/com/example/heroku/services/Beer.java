package com.example.heroku.services;

import com.example.heroku.model.BeerUnit;
import com.example.heroku.model.Image;
import com.example.heroku.model.repository.BeerRepository;
import com.example.heroku.model.repository.BeerUnitRepository;
import com.example.heroku.model.repository.ImageRepository;
import com.example.heroku.model.repository.SearchTokenRepository;
import com.example.heroku.request.beer.BeerInfo;
import com.example.heroku.request.beer.BeerSubmitData;
import com.example.heroku.response.Format;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.ArrayList;

import static org.springframework.http.ResponseEntity.ok;

@Component
public class Beer {
    @Autowired
    SearchTokenRepository searchBeer;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerUnitRepository beerUnitRepository;

    public Mono<ResponseEntity> generateID() {
        return Mono.just(ok(Format.builder().response(Util.getInstance().GenerateID()).build()));
    }

    public Flux<BeerUnit> CreateBeer(@Valid @ModelAttribute BeerInfo info) {
        return Mono.just(info)
                .flatMap(beerInfo ->
                        this.beerRepository.deleteBySecondId(beerInfo.getBeer().getBeer_second_id())
                )
                .then(Mono.just(info)
                        .flatMap(beerInfo ->
                                this.beerRepository.save(beerInfo.getBeer().UpdateMetaSearch().AutoFill())
                        )
                )
                .flatMap(beer ->
                        this.searchBeer.deleteBySecondId(beer.getBeer_second_id())
                )
                .then(Mono.just(info)
                        .flatMap(beerInfo ->
                                this.searchBeer.saveToken(beerInfo.getBeer().getBeer_second_id(),
                                        beerInfo.getBeer().getTokens()
                                        )
                        )
                )
                .then(Mono.just(info)
                        .flatMap(
                                beerInfo ->
                                        this.beerUnitRepository.deleteByBeerId(info.getBeer().getBeer_second_id())
                        )
                )
                .thenMany(Flux.just(info.getBeerUnit())
                        .flatMap(beerUnit ->
                                this.beerUnitRepository.save(beerUnit.AutoFill())
                        )
                );
    }

    public Mono<BeerInfo> GetBeerByID(String id){
        return beerRepository.findBySecondID(id)
                .map(beer ->
                        BeerInfo.builder().beer(beer).build()
                )
                .flatMap(beerInfo ->
                    Mono.just(new ArrayList<BeerUnit>())
                            .flatMap(beerUnits ->
                                    beerUnitRepository.findByBeerID(id)
                                            .map(beerUnits::add
                                            )
                                            .then(
                                                    Mono.just(beerUnits)
                                            )
                                            .map(beerInfo::SetBeerUnit
                                            )
                            )
                );
    }

    public Flux<BeerSubmitData> GetAllBeer(int page, int size){
        return this.beerRepository.findAll(page, size)
                .flatMap(this::CoverToSubmitData);
    }

    public Mono<BeerSubmitData> CoverToSubmitData(com.example.heroku.model.Beer beer) {
        return Mono.just(BeerSubmitData.builder().build().FromBeer(beer))
                .flatMap(beerSubmitData ->
                        Mono.just(new ArrayList<BeerUnit>())
                                .flatMap(beerUnits ->
                                        beerUnitRepository.findByBeerID(beerSubmitData.getBeerSecondID())
                                                .map(beerUnits::add)
                                                .then(
                                                        Mono.just(beerUnits)
                                                )
                                                .map(beerSubmitData::SetBeerUnit)
                                )
                )
                .flatMap(beerSubmitData ->
                        imageRepository.findByCategory(beerSubmitData.getBeerSecondID())
                                .map(image -> beerSubmitData.AddImage(image.getImgid()))
                                .then(Mono.just(beerSubmitData))
                );
    }

    public Flux<BeerSubmitData> SearchBeer(String txt, int page, int size) {
        txt = Util.getInstance().RemoveAccent(txt);
        if (txt.contains("&"))
            return this.beerRepository.searchBeer(txt, page, size)
                    .flatMap(this::CoverToSubmitData);
        return this.beerRepository.searchBeerLike("%" + txt + "%", page, size)
                .flatMap(this::CoverToSubmitData);
    }

    public Flux<com.example.heroku.model.Beer> GetAllBeerByCategory(com.example.heroku.model.Beer.Category category, int page, int size){
        Sort sort = Sort.by(Sort.Direction.DESC, "createat");
        return this.beerRepository.findByCategory(category, PageRequest.of(page, size, sort));
    }
}
