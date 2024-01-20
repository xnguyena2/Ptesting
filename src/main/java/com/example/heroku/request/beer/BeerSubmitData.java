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
import java.util.Arrays;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeerSubmitData {

    private String group_id;
    private String beerSecondID;
    private String name;
    private String detail;
    private String sku;
    private String upc;
    private String meta_search;
    private String category;
    private String status;
    private List<Image> images;
    private BeerUnit[] listUnit;

    public BeerInfo GetBeerInfo() {

        List<ProductUnit> listMapedUnit = new ArrayList<>();
        for (BeerUnit beerUnit : this.listUnit) {
            listMapedUnit.add(ProductUnit.builder()
                    .name(beerUnit.name)
                    .group_id(this.group_id)
                    .product_second_id(this.beerSecondID)
                    .price(beerUnit.price)
                    .wholesale_price(beerUnit.wholesale_price)
                    .wholesale_number(beerUnit.wholesale_number)
                    .buy_price(beerUnit.buy_price)
                    .discount(beerUnit.discount)
                    .volumetric(beerUnit.volumetric)
                    .weight(beerUnit.weight)
                    .product_unit_second_id(beerUnit.beer_unit_second_id)
                    .status(ProductUnit.Status.get(beerUnit.status))
                    .date_expire(beerUnit.GetExpirDateTime())
                    .build());
        }

        return BeerInfo
                .builder()
                .product(
                        Product
                                .builder()
                                .group_id(this.group_id)
                                .product_second_id(this.beerSecondID)
                                .name(this.name)
                                .detail(this.detail)
                                .sku(this.sku)
                                .upc(this.upc)
                                .category(this.category)
                                .status(Product.Status.get(this.status))
                                .meta_search(this.meta_search)
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

    public static BeerSubmitData FromProductInfo(BeerInfo info) {
        BeerSubmitData beerSubmitData = FromBeer(info.getProduct());
        beerSubmitData.SetBeerUnit(Arrays.asList(info.getProductUnit()));
        return beerSubmitData;
    }

    public static BeerSubmitData FromBeer(Product product) {
        return BeerSubmitData.builder()
                .group_id(product.getGroup_id())
                .beerSecondID(product.getProduct_second_id())
                .name(product.getName())
                .detail(product.getDetail())
                .sku(product.getSku())
                .upc(product.getUpc())
                .category(product.getCategory())
                .status(product.GetStatusNuable().getName())
                .meta_search(product.getMeta_search())
                .build();
    }

    public BeerSubmitData SetBeerUnit(List<ProductUnit> productUnitList) {
        listUnit = new BeerUnit[productUnitList.size()];
        for (int i = 0; i < listUnit.length; i++) {
            ProductUnit item = productUnitList.get(i);
            listUnit[i] = BeerUnit.builder()
                    .group_id(item.getGroup_id())
                    .beer(item.getProduct_second_id())
                    .name(item.getName())
                    .price(item.getPrice())
                    .wholesale_price(item.getWholesale_price())
                    .wholesale_number(item.getWholesale_number())
                    .buy_price(item.getBuy_price())
                    .discount(item.getDiscount())
                    .dateExpir(NgbDateStruct.FromTimestamp(item.getDate_expire()))
                    .volumetric(item.getVolumetric())
                    .weight(item.getWeight())
                    .beer_unit_second_id(item.getProduct_unit_second_id())
                    .status(item.GetStatusNuable().toString())
                    .build();
        }
        return this;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BeerUnit {
        private String group_id;
        private String beer;
        private String name;
        private float price;
        private float wholesale_price;
        private int wholesale_number;
        private float buy_price;
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
