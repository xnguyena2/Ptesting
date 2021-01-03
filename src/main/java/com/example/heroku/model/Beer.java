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
@Table(name="beer")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Beer {

    @Id
    String id;

    private String beer_second_id;

    private String name;

    private String detail;

    private String meta_search;

    private Category category;

    private Timestamp createat;

    public String getTokens() {
        if(name == null && detail == null)
            return "";
        if(name == null)
            return Util.getInstance().RemoveAccent(detail);
        if(detail == null)
            return Util.getInstance().RemoveAccent(name);
        return Util.getInstance().RemoveAccent(name + " " + detail);
    }

    public Beer UpdateMetaSearch(){
        this.meta_search = this.getTokens();
        return this;
    }

    public Beer AutoFill(){
        this.createat = new Timestamp(new Date().getTime());
        return this;
    }

    public enum Category {
        ALCOHOL("alcohol_drinks"),
        BEVERAGE("beverage"),
        FRESH_WATER("fresh_water"),
        INTERNATIONNAL_DRINKS("internationnal_drinks"),
        JUICE_FRUIT("juice_fruit");

        private String name;

        Category(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        private static final Map<String, Category> lookup = new HashMap<>();

        static
        {
            for(Category env : Category.values())
            {
                lookup.put(env.getName(), env);
            }
        }

        public static Category get(String text)
        {
            return lookup.get(text);
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}