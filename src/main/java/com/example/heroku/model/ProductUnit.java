package com.example.heroku.model;

import com.example.heroku.model.entity.BaseEntity;
import com.example.heroku.util.Util;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="product_unit")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductUnit extends BaseEntity {

    private String product_unit_second_id;

    private String product_second_id;

    private String name;

    private String sku;

    private String upc;

    private float price;

    private float promotional_price;

    private float inventory_number;

    private int wholesale_number;

    private float wholesale_price;

    private float buy_price;

    private float discount;

    private LocalDateTime date_expire;

    private float volumetric;

    private float weight;

    private boolean visible;

    private boolean enable_warehouse;

    private boolean enable_serial;

    private Product.ProductType product_type;

    private boolean has_component;

    private String arg_action_id;

    private String arg_action_type;

    private String group_unit_naname;

    private String group_unit_id;

    private float group_unit_number;

    private String services_config;

    private String[] list_product_serial_id;

    private Status status;

    /***
     * {@link com.example.heroku.model.joinwith.ProductJoinWithProductUnit}
     */
    /***
     * {@link com.example.heroku.request.beer.BeerSubmitData}
     */

    public enum Status{
        AVARIABLE("AVARIABLE"),
        NOT_FOR_SELL("NOT_FOR_SELL"),
        SOLD_OUT("SOLD_OUT");



        private String name;

        Status(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }

        private static final Map<String, Status> lookup = new HashMap<>();

        static
        {
            for(Status sts : Status.values())
            {
                lookup.put(sts.getName(), sts);
            }
        }

        public static Status get(String text)
        {
            try {
                Status val = lookup.get(text);
                if(val == null){
                    return AVARIABLE;
                }
                return val;
            } catch (Exception ex) {
                ex.printStackTrace();
                return AVARIABLE;
            }
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    public Status GetStatusNuable() {
        if (status == null) {
            return Status.AVARIABLE;
        }
        return status;
    }


    public ProductUnit AutoFill() {
        super.AutoFill();
        if (this.product_unit_second_id == null || this.product_unit_second_id.isEmpty())
            this.product_unit_second_id = Util.getInstance().GenerateID();
        return this;
    }

    public ProductUnit UpdateToRealPrice() {
        price *= (100 - discount) / 100;
        return this;
    }

    public ProductUnit CheckDiscount() {
        if (date_expire == null)
            return this;
        LocalDateTime currentTime = Util.getInstance().Now();
        int diff = Util.getInstance().DiffirentDays(date_expire, currentTime);
        System.out.println("Check Discount date expire: " + date_expire.toString() + ", current time: " + currentTime + ", diff: " + diff);
        if (diff < 0) {
            this.discount = 0;
        }
        return this;
    }
}

