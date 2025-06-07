package com.example.heroku.model.joinwith;

import com.example.heroku.model.Image;
import com.example.heroku.model.Product;
import com.example.heroku.model.ProductUnit;
import com.example.heroku.request.beer.BeerSubmitData;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductJoinWithProductUnit extends Product {

    /***
     * {@link ProductUnit}
     */
    /***
     * {@link Image}
     */


    protected Long child_id;
    protected String child_group_id;
    protected Timestamp child_createat;
    private String child_product_unit_second_id;
    private String child_product_second_id;
    private String child_name;
    private String child_sku;
    private String child_upc;
    private float child_price;
    private float child_promotional_price;
    private float child_inventory_number;
    private int child_wholesale_number;
    private float child_wholesale_price;
    private float child_buy_price;
    private float child_discount;
    private Timestamp child_date_expire;
    private float child_volumetric;
    private float child_weight;
    private boolean child_visible;
    private boolean child_enable_warehouse;
    private String child_group_unit_id;
    private String child_group_unit_naname;
    private float child_group_unit_number;
    private ProductType child_product_type;
    private String child_services_config;
    private String child_arg_action_id;
    private String child_arg_action_type;
    private ProductUnit.Status child_status;



    // image of product unit
    protected Long img_id;
    protected String img_group_id;
    protected Timestamp img_createat;
    private String imgid;
    private String tag;
    private String thumbnail;
    private String medium;
    private String large;
    private String category;


    public String getID() {
        return getProduct_second_id();
    }

    public Product getParent() {
        return super.toBuilder().build();
    }

    public ProductUnit getChild() {
        return ProductUnit.builder()
                .id(child_id)
                .group_id(child_group_id)
                .createat(child_createat)
                .product_unit_second_id(child_product_unit_second_id)
                .product_second_id(child_product_second_id)
                .name(child_name)
                .sku(child_sku)
                .upc(child_upc)
                .price(child_price)
                .promotional_price(child_promotional_price)
                .inventory_number(child_inventory_number)
                .wholesale_number(child_wholesale_number)
                .wholesale_price(child_wholesale_price)
                .buy_price(child_buy_price)
                .discount(child_discount)
                .date_expire(child_date_expire)
                .volumetric(child_volumetric)
                .weight(child_weight)
                .visible(child_visible)
                .enable_warehouse(child_enable_warehouse)
                .group_unit_id(child_group_unit_id)
                .group_unit_naname(child_group_unit_naname)
                .group_unit_number(child_group_unit_number)
                .product_type(child_product_type)
                .services_config(child_services_config)
                .arg_action_id(child_arg_action_id)
                .arg_action_type(child_arg_action_type)
                .status(child_status)
                .build();
    }

    public static BeerSubmitData GenerateBeerSubmitData(List<ProductJoinWithProductUnit> listData) {
        if (listData.isEmpty()) {
            return BeerSubmitData.Empty();
        }
        ProductJoinWithProductUnit firstElement = listData.get(0);
        BeerSubmitData result = BeerSubmitData.FromBeer(firstElement.getParent());
        List<ProductUnit> productUnitList = new ArrayList<ProductUnit>();
        List<Image> productUnitImageList = new ArrayList<Image>();
        for (ProductJoinWithProductUnit child : listData) {
            ProductUnit productUnit = child.getChild();
            if (productUnit.getProduct_second_id() == null || productUnit.getProduct_second_id().isEmpty()) {
                continue;
            }
            productUnitList.add(productUnit);

            if (child.getImgid() == null || child.getImgid().isEmpty()) {
                continue;
            }
            Image img =  Image.builder()
                    .id(child.getImg_id())
                    .group_id(child.getImg_group_id())
                    .createat(child.getImg_createat())
                    .imgid(child.getImgid())
                    .tag(child.getTag())
                    .thumbnail(child.getThumbnail())
                    .medium(child.getMedium())
                    .large(child.getLarge())
                    .category(child.getCategory())
                    .build();

            productUnitImageList.add(img);
        }
        result.setImages(productUnitImageList);
        result.SetBeerUnit(productUnitList);
        return result;
    }

    public static BeerSubmitData GenerateBeerSubmitDataAllImg(List<ProductJoinWithProductUnit> listData) {
        if (listData.isEmpty()) {
            return BeerSubmitData.Empty();
        }
        ProductJoinWithProductUnit firstElement = listData.get(0);
        BeerSubmitData result = BeerSubmitData.FromBeer(firstElement.getParent());
        Map<String, ProductUnit> productUnitList = new HashMap<>();
        Map<String, Image> productUnitImageList = new HashMap<>();
        for (ProductJoinWithProductUnit child : listData) {
            ProductUnit productUnit = child.getChild();
            if (productUnit.getProduct_second_id() == null || productUnit.getProduct_second_id().isEmpty()) {
                continue;
            }
            productUnitList.put(productUnit.getProduct_unit_second_id(), productUnit);

            if (child.getImgid() == null || child.getImgid().isEmpty()) {
                continue;
            }
            Image img =  Image.builder()
                    .id(child.getImg_id())
                    .group_id(child.getImg_group_id())
                    .createat(child.getImg_createat())
                    .imgid(child.getImgid())
                    .tag(child.getTag())
                    .thumbnail(child.getThumbnail())
                    .medium(child.getMedium())
                    .large(child.getLarge())
                    .category(child.getCategory())
                    .build();

            productUnitImageList.put(img.getId().toString() ,img);
        }
        result.setImages(new ArrayList<>(productUnitImageList.values()));
        result.SetBeerUnit(new ArrayList<>(productUnitList.values()));
        return result;
    }
}
