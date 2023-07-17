package com.example.heroku.request.beer;

import com.example.heroku.model.Product;
import com.example.heroku.model.Image;
import com.example.heroku.model.ProductUnit;
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

        List<ProductUnit> listMapedUnit = new ArrayList<>();
        for (BeerUnit beerUnit : this.listUnit) {
            listMapedUnit.add(ProductUnit.builder()
                    .name(beerUnit.name)
                    .beer(this.beerSecondID)
                    .price(beerUnit.price)
                    .discount(beerUnit.discount)
                    .volumetric(beerUnit.volumetric)
                    .weight(beerUnit.weight)
                    .beer_unit_second_id(beerUnit.beer_unit_second_id)
                    .status(ProductUnit.Status.get(beerUnit.status))
                    .date_expire(beerUnit.GetExpirDateTime())
                    .build());
        }

        return BeerInfo
                .builder()
                .product(
                        Product
                                .builder()
                                .beer_second_id(this.beerSecondID)
                                .name(this.name)
                                .detail(this.detail)
                                .category(Product.Category.get(this.category))
                                .status(Product.Status.get(this.status))
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

    public BeerSubmitData FromBeer(Product product) {
        return BeerSubmitData.builder()
                .beerSecondID(product.getBeer_second_id())
                .name(product.getName())
                .detail(product.getDetail())
                .category(product.GetCategoryNuable().getName())
                .status(product.GetStatusNuable().getName())
                .build();
    }

    public BeerSubmitData SetBeerUnit(List<ProductUnit> productUnitList) {
        listUnit = new BeerUnit[productUnitList.size()];
        for (int i = 0; i < listUnit.length; i++) {
            BeerUnit newB = new BeerUnit();
            newB.setBeer(productUnitList.get(i).getBeer());
            newB.setName(productUnitList.get(i).getName());
            newB.setPrice(productUnitList.get(i).getPrice());
            newB.setDiscount(productUnitList.get(i).getDiscount());
            newB.setDateExpir(NgbDateStruct.FromTimestamp(productUnitList.get(i).getDate_expire()));
            newB.setVolumetric(productUnitList.get(i).getVolumetric());
            newB.setWeight(productUnitList.get(i).getWeight());
            newB.setBeer_unit_second_id(productUnitList.get(i).getBeer_unit_second_id());
            newB.setStatus(productUnitList.get(i).GetStatusNuable().toString());
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
        private String status;

        public Timestamp GetExpirDateTime() {
            if (dateExpir == null)
                return null;
            if (dateExpir.getDay() == 0 && dateExpir.getMonth() == 0 && dateExpir.getYear() == 0)
                return null;
            return dateExpir.ToDateTime();
        }
    }
}
