package com.example.heroku.request.beer;

import com.example.heroku.model.Beer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
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
                                .build()
                )
                .build()
                .SetBeerUnit(listMapedUnit);
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

        public Timestamp GetExpirDateTime() {
            if (dateExpir == null)
                return null;
            Calendar cal = Calendar.getInstance();
            cal.set(dateExpir.year, dateExpir.month, dateExpir.day);
            return new Timestamp(cal.getTime().getTime());
        }


        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class NgbDateStruct {
            private int day;
            private int month;
            private int year;
        }
    }
}
