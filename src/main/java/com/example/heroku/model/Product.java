package com.example.heroku.model;

import com.example.heroku.util.Util;
import com.example.heroku.model.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="product")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {

    private String product_second_id;

    private String name;

    private String detail;

    private String meta_search;

    private String category;

    private String unit_category_config;

    private Status status;

    private boolean visible_web;

    private ProductType product_type;

    private String default_group_unit_naname;

    private String number_group_unit_config;

    private String warranty;

    //update ProductJoinWithProductUnit
    //update BeerSubmitData

    private String appendToken(String token, String appendTxt) {
        if (appendTxt != null && !appendTxt.isEmpty()) {
            if (!token.isEmpty()) {
                token += " ";
            }
            token += appendTxt;
        }
        return token;
    }

    public String getTokens() {
        String token = "";
        token = appendToken(token, name);
        token = appendToken(token, detail);
        if (token.isEmpty()) {
            return token;
        }
        return Util.getInstance().RemoveAccent(token);
    }

    public Product UpdateMetaSearch() {
        this.meta_search = this.getTokens();
        return this;
    }

    public Product AutoFill() {
        if (this.product_second_id == null || this.product_second_id.isEmpty())
            this.product_second_id = Util.getInstance().GenerateID();
        super.AutoFill();
        return this;
    }

    public Status GetStatusNuable() {
        if (status == null) {
            return Status.AVARIABLE;
        }
        return status;
    }

    public enum Status {
        AVARIABLE("AVARIABLE"),
        NOT_FOR_SELL("NOT_FOR_SELL"),
        SOLD_OUT("SOLD_OUT"),
        HIDE("HIDE");


        private String name;

        Status(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        private static final Map<String, Status> lookup = new HashMap<>();

        static {
            for (Status sts : Status.values()) {
                lookup.put(sts.getName(), sts);
            }
        }

        public static Status get(String text) {
            try {
                Status val = lookup.get(text);
                if (val == null) {
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

    public enum ProductType {
        PRODUCT("PRODUCT"),
        COMBO("COMBO"),
        MATERIAL("MATERIAL");


        private String name;

        ProductType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        private static final Map<String, ProductType> lookup = new HashMap<>();

        static {
            for (ProductType sts : ProductType.values()) {
                lookup.put(sts.getName(), sts);
            }
        }

        public static ProductType get(String text) {
            try {
                ProductType val = lookup.get(text);
                if (val == null) {
                    return PRODUCT;
                }
                return val;
            } catch (Exception ex) {
                ex.printStackTrace();
                return PRODUCT;
            }
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}