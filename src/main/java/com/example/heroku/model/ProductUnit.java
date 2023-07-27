package com.example.heroku.model;

import com.example.heroku.util.Util;
import entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;
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

    @Id
    String id;

    private String product_unit_second_id;

    private String product_second_id;

    private String name;

    private float price;

    private float discount;

    private Timestamp date_expire;

    private float volumetric;

    private float weight;

    private Status status;

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
        if (this.product_unit_second_id == null || this.product_unit_second_id.equals(""))
            this.product_unit_second_id = Util.getInstance().GenerateID();
        this.createat = new Timestamp(new Date().getTime());
        return this;
    }

    public ProductUnit UpdateToRealPrice() {
        price *= (100 - discount) / 100;
        return this;
    }

    public ProductUnit CheckDiscount() {
        if (date_expire == null)
            return this;
        Timestamp currentTime = new Timestamp(new Date().getTime());
        int diff = Util.getInstance().DiffirentDays(date_expire, currentTime);
        System.out.println("Check Discount date expire: " + date_expire.toString() + ", current time: " + currentTime + ", diff: " + diff);
        if (diff < 0) {
            this.discount = 0;
        }
        return this;
    }
}

