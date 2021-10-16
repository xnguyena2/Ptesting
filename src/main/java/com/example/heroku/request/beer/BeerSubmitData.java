package com.example.heroku.request.beer;

import com.example.heroku.model.Beer;
import com.example.heroku.model.Image;
import com.example.heroku.request.datetime.NgbDateStruct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeerSubmitData {

    private String beerSecondID;
    private String name;
    private String detail;
    private String category;
    private String status;
    private List<Image> images;
    private BeerUnit[] listUnit;

    public BeerInfo GetBeerInfo() {

        List<com.example.heroku.model.BeerUnit> listMapedUnit = new ArrayList<>();
        for (BeerUnit beerUnit : this.listUnit) {
            listMapedUnit.add(com.example.heroku.model.BeerUnit.builder()
                    .name(beerUnit.name)
                    .beer(this.beerSecondID)
                    .price(beerUnit.price)
                    .discount(beerUnit.discount)
                    .volumetric(beerUnit.volumetric)
                    .weight(beerUnit.weight)
                    .beer_unit_second_id(beerUnit.beer_unit_second_id)
                    .date_expire(beerUnit.GetExpirDateTime())
                    .build());
        }

        return BeerInfo
                .builder()
                .beer(
                        Beer
                                .builder()
                                .beer_second_id(this.beerSecondID)
                                .name(this.name)
                                .detail(this.detail)
                                .category(Beer.Category.get(this.category))
                                .status(Beer.Status.get(this.status))
                                .build()
                )
                .build()
                .SetBeerUnit(listMapedUnit);
    }

    public BeerSubmitData AddImage(Image image) {
        if (this.images == null) {
            this.images = new ArrayList<>();
        }
        this.images.add(image);
        return this;
    }

    public BeerSubmitData FromBeer(Beer beer) {
        return BeerSubmitData.builder()
                .beerSecondID(beer.getBeer_second_id())
                .name(beer.getName())
                .detail(beer.getDetail())
                .category(beer.GetCategoryNuable().getName())
                .status(beer.GetStatusNuable().getName())
                .build();
    }

    public BeerSubmitData SetBeerUnit(List<com.example.heroku.model.BeerUnit> beerUnitList) {
        listUnit = new BeerUnit[beerUnitList.size()];
        for (int i = 0; i < listUnit.length; i++) {
            BeerUnit newB = new BeerUnit();
            newB.setBeer(beerUnitList.get(i).getBeer());
            newB.setName(beerUnitList.get(i).getName());
            newB.setPrice(beerUnitList.get(i).getPrice());
            newB.setDiscount(beerUnitList.get(i).getDiscount());
            newB.setDateExpir(NgbDateStruct.FromTimestamp(beerUnitList.get(i).getDate_expire()));
            newB.setVolumetric(beerUnitList.get(i).getVolumetric());
            newB.setWeight(beerUnitList.get(i).getWeight());
            newB.setBeer_unit_second_id(beerUnitList.get(i).getBeer_unit_second_id());
            listUnit[i] = newB;
        }
        return this;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BeerUnit {
        private String beer;
        private String name;
        private float price;
        private float discount;
        private NgbDateStruct dateExpir;
        private float volumetric;
        private float weight;
        private String beer_unit_second_id;

        public Timestamp GetExpirDateTime() {
            if (dateExpir == null)
                return null;
            if (dateExpir.getDay() == 0 && dateExpir.getMonth() == 0 && dateExpir.getYear() == 0)
                return null;
            return dateExpir.ToDateTime();
        }
    }
}
