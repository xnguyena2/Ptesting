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
@Table(name="product")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    String id;

    private String product_second_id;

    private String name;

    private String detail;

    private String meta_search;

    private String category;

    private Timestamp createat;

    private Status status;

    public String getTokens() {
        if(name == null && detail == null)
            return "";
        if(name == null)
            return Util.getInstance().RemoveAccent(detail);
        if(detail == null)
            return Util.getInstance().RemoveAccent(name);
        return Util.getInstance().RemoveAccent(name + " " + detail);
    }

    public Product UpdateMetaSearch(){
        this.meta_search = this.getTokens();
        return this;
    }

    public Product AutoFill(){
        this.createat = new Timestamp(new Date().getTime());
        return this;
    }

    public Status GetStatusNuable() {
        if (status == null) {
            return Status.AVARIABLE;
        }
        return status;
    }

    public enum Status {
        AVARIABLE("avariable"),
        NOT_FOR_SELL("not_for_sell"),
        SOLD_OUT("sold_out"),
        HIDE("hide");



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
}