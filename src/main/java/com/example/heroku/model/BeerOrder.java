package com.example.heroku.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name="beer_order")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeerOrder {
    @Id
    String id;

    private String beer_id;

    private int beer_unit;

    private String user_id;

    private String voucher_second_id;

    private int number_unit;

    private float total_price;

    private float ship_price;

    private Status status;

    private Timestamp createat;


    public enum Status{
        ORDER("ORDER"),
        SENDING("SENDING"),
        DONE("DONE");

        private String name;

        Status(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }

        private static final Map<String, Beer.Status> lookup = new HashMap<>();

        static
        {
            for(Beer.Status sts : Beer.Status.values())
            {
                lookup.put(sts.getName(), sts);
            }
        }

        public static Beer.Status get(String text)
        {
            return lookup.get(text);
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

}
