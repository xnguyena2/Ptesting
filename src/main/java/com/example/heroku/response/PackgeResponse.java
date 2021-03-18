package com.example.heroku.response;

import com.example.heroku.model.UserPackage;
import com.example.heroku.request.beer.BeerSubmitData;
import lombok.*;
import reactor.core.publisher.Mono;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackgeResponse extends UserPackage {

    public PackgeResponse(UserPackage s){
        super(s);
    }

    private BeerSubmitData beerSubmitData;

    public PackgeResponse SetBeerData(BeerSubmitData b){
        //b.doOnNext(this::setBeerSubmitData).subscribe();
        this.beerSubmitData = b;
        return this;
    }
}
