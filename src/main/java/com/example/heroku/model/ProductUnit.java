package com.example.heroku.model;

import com.example.heroku.util.Util;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name="beer_unit")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductUnit {

    @Id
    String id;

    private String beer_unit_second_id;

    private String beer;

    private String name;

    private float price;

    private float discount;

    private Timestamp date_expire;

    private float volumetric;

    private float weight;

    private Timestamp createat;

    private Status status;

    public enum Status{
        AVARIABLE("avariable"),
        NOT_FOR_SELL("not_for_sell"),
        SOLD_OUT("sold_out");



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
        if (this.beer_unit_second_id == null || this.beer_unit_second_id.equals(""))
            this.beer_unit_second_id = Util.getInstance().GenerateID();
        this.createat = new Timestamp(new Date().getTime());
        return this;
    }

    public ProductUnit UpdateToRealPrice() {
        System.out.println("Update price");
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

