package com.example.heroku.model.joinwith;

import com.example.heroku.model.Product;
import com.example.heroku.model.ProductUnit;
import com.example.heroku.request.beer.BeerSubmitData;
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
public class ProductJoinWithProductUnit {

    //Product


    protected Long id;
    protected String group_id;
    protected Timestamp createat;
    private String product_second_id;
    private String name;
    private String detail;
    private String meta_search;
    private String category;
    private String unit_category_config;
    private Product.Status status;
    private boolean visible_web;



    // ProductUnit


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
    private int child_inventory_number;
    private int child_wholesale_number;
    private float child_wholesale_price;
    private float child_buy_price;
    private float child_discount;
    private Timestamp child_date_expire;
    private float child_volumetric;
    private float child_weight;
    private boolean child_visible;
    private boolean child_enable_warehouse;
    private ProductUnit.Status child_status;


    public String getID() {
        return product_second_id;
    }

    public Product getParent() {
        return Product.builder()
                .id(id)
                .group_id(group_id)
                .createat(createat)
                .product_second_id(product_second_id)
                .name(name)
                .detail(detail)
                .meta_search(meta_search)
                .category(category)
                .unit_category_config(unit_category_config)
                .visible_web(visible_web)
                .status(status)
                .build();
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
        for (ProductJoinWithProductUnit child : listData) {
            ProductUnit productUnit = child.getChild();
            if (productUnit.getProduct_second_id() == null || productUnit.getProduct_second_id().isEmpty()) {
                continue;
            }
            productUnitList.add(productUnit);
        }
        result.SetBeerUnit(productUnitList);
        return result;
    }
}
