package com.example.heroku.request.beer;

import com.example.heroku.model.Product;
import com.example.heroku.model.Image;
import com.example.heroku.model.ProductComboItem;
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
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeerSubmitData {

    private String group_id;
    private String beerSecondID;
    private String product_parent_id;
    private String name;
    private String detail;
    private String meta_search;
    private String category;
    private String unit_category_config;
    private String status;
    private boolean visible_web;
    private Product.ProductType product_type;
    private List<Image> images;
    private BeerUnit[] listUnit;
    private ProductComboItem[] listComboItem;
    private String default_group_unit_naname;
    private String number_group_unit_config;
    private String warranty;

    public BeerInfo GetBeerInfo() {

        List<ProductUnit> listMapedUnit = new ArrayList<>();
        for (BeerUnit beerUnit : this.listUnit) {
            listMapedUnit.add(ProductUnit.builder()
                    .name(beerUnit.name)
                    .group_id(this.group_id)
                    .product_second_id(this.beerSecondID)
                    .sku(beerUnit.sku)
                    .upc(beerUnit.upc)
                    .price(beerUnit.price)
                    .wholesale_price(beerUnit.wholesale_price)
                    .wholesale_number(beerUnit.wholesale_number)
                    .promotional_price(beerUnit.promotional_price)
                    .inventory_number(beerUnit.inventory_number)
                    .buy_price(beerUnit.buy_price)
                    .discount(beerUnit.discount)
                    .volumetric(beerUnit.volumetric)
                    .weight(beerUnit.weight)
                    .product_unit_second_id(beerUnit.beer_unit_second_id)
                    .visible(beerUnit.visible)
                    .enable_warehouse(beerUnit.enable_warehouse)
                    .enable_serial(beerUnit.enable_serial)
                    .list_product_serial_id(beerUnit.list_product_serial_id)
                    .product_type(beerUnit.product_type)
                    .group_unit_id(beerUnit.group_unit_id)
                    .group_unit_number(beerUnit.group_unit_number)
                    .group_unit_naname(beerUnit.group_unit_naname)
                    .services_config(beerUnit.services_config)
                    .arg_action_id(beerUnit.getArg_action_id())
                    .arg_action_type(beerUnit.getArg_action_type())
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
                                .product_parent_id(this.product_parent_id)
                                .name(this.name)
                                .detail(this.detail)
                                .category(this.category)
                                .unit_category_config(this.unit_category_config)
                                .status(Product.Status.get(this.status))
                                .meta_search(this.meta_search)
                                .visible_web(this.visible_web)
                                .product_type(this.product_type)
                                .default_group_unit_naname(this.default_group_unit_naname)
                                .number_group_unit_config(this.number_group_unit_config)
                                .warranty(this.warranty)
                                .build()
                )
                .listComboItem(listComboItem)
                .build()
                .SetBeerUnit(listMapedUnit);
    }

    public static BeerSubmitData FromBeer(Product product) {
        return BeerSubmitData.builder()
                .group_id(product.getGroup_id())
                .beerSecondID(product.getProduct_second_id())
                .product_parent_id(product.getProduct_parent_id())
                .name(product.getName())
                .detail(product.getDetail())
                .category(product.getCategory())
                .unit_category_config(product.getUnit_category_config())
                .status(product.GetStatusNuable().getName())
                .meta_search(product.getMeta_search())
                .visible_web(product.isVisible_web())
                .product_type(product.getProduct_type())
                .default_group_unit_naname(product.getDefault_group_unit_naname())
                .number_group_unit_config(product.getNumber_group_unit_config())
                .warranty(product.getWarranty())
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
                    .sku(item.getSku())
                    .upc(item.getUpc())
                    .price(item.getPrice())
                    .wholesale_price(item.getWholesale_price())
                    .wholesale_number(item.getWholesale_number())
                    .promotional_price(item.getPromotional_price())
                    .inventory_number(item.getInventory_number())
                    .buy_price(item.getBuy_price())
                    .discount(item.getDiscount())
                    .dateExpir(NgbDateStruct.FromTimestamp(item.getDate_expire()))
                    .volumetric(item.getVolumetric())
                    .weight(item.getWeight())
                    .beer_unit_second_id(item.getProduct_unit_second_id())
                    .visible(item.isVisible())
                    .enable_warehouse(item.isEnable_warehouse())
                    .enable_serial(item.isEnable_serial())
                    .list_product_serial_id(item.getList_product_serial_id())
                    .product_type(item.getProduct_type())
                    .group_unit_id(item.getGroup_unit_id())
                    .group_unit_number(item.getGroup_unit_number())
                    .group_unit_naname(item.getGroup_unit_naname())
                    .services_config(item.getServices_config())
                    .arg_action_id(item.getArg_action_id())
                    .arg_action_type(item.getArg_action_type())
                    .status(item.GetStatusNuable().toString())
                    .build();
        }
        return this;
    }

    public BeerSubmitData AddImage(Image image) {
        if (this.images == null) {
            this.images = new ArrayList<>();
        }
        this.images.add(image);
        return this;
    }

    public BeerSubmitData SetImg(Map<String, List<Image>> mapImg) {
        this.images = mapImg.get(beerSecondID);
        return this;
    }

    public BeerSubmitData SetProductImg(Map<String, Image> mapImg) {
        Image pImg = mapImg.get(beerSecondID);
        if (pImg == null) {
            return this;
        }
        this.AddImage(pImg);
        return this;
    }

    public static BeerSubmitData FromProductInfo(BeerInfo info) {
        BeerSubmitData beerSubmitData = FromBeer(info.getProduct());
        beerSubmitData.SetBeerUnit(Arrays.asList(info.getProductUnit()));
        return beerSubmitData;
    }

    public static BeerSubmitData Empty() {
        return BeerSubmitData.builder().build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BeerUnit {
        private String group_id;
        private String beer;
        private String name;
        private String sku;
        private String upc;
        private float price;
        private float wholesale_price;
        private int wholesale_number;
        private float promotional_price;
        private float inventory_number;
        private float buy_price;
        private float discount;
        private NgbDateStruct dateExpir;
        private float volumetric;
        private float weight;
        private String beer_unit_second_id;
        private boolean visible;
        private boolean enable_warehouse;
        private boolean enable_serial;
        private Product.ProductType product_type;
        private String group_unit_naname;
        private String group_unit_id;
        private float group_unit_number;
        private String services_config;
        private String arg_action_id;
        private String arg_action_type;
        private String[] list_product_serial_id;
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
