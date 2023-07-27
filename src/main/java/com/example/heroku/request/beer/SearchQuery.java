package com.example.heroku.request.beer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchQuery {

    public enum Filter {
        DEFAULT("default"),
        PRICE_ASC("price_asc"),
        PRICE_DESC("price_desc"),
        NAME_ASC("name_asc"),
        NAME_DESC("name_desc"),
        CREATE_DESC("create_desc"),
        CREATE_ASC("create_asc"),
        SOLD_NUM("sold_num");

        private final String name;

        Filter(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        private static final Map<String, Filter> lookup = new HashMap<>();

        static {
            for (Filter ctg : Filter.values()) {
                lookup.put(ctg.getName(), ctg);
            }
        }

        public static Filter get(String text) {
            try {
                Filter val = lookup.get(text);
                if (val == null) {
                    return DEFAULT;
                }
                return val;
            } catch (Exception ex) {
                ex.printStackTrace();
                return DEFAULT;
            }
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    private String group_id;
    private String query;
    private int page;
    private int size;
    private String filter;

    public String GetFilterTxt() {
        return filter;
    }

    public Filter GetFilter() {
        return Filter.get(filter);
    }
}
