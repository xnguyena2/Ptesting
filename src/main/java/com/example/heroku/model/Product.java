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
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {

    private String product_second_id;

    private String name;

    private String detail;

    private String sku;

    private String upc;

    private String meta_search;

    private String category;

    private Status status;

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
        token = appendToken(token, sku);
        token = appendToken(token, upc);
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
}