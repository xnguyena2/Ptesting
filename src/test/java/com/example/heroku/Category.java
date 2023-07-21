package com.example.heroku;


import java.util.HashMap;
import java.util.Map;

public enum Category {
    CRAB("crab"),
    SHRIMP("shrimp"),
    SQUID("squid"),
    HOLOTHURIAN("holothurian"),
    HALIOTIS("haliotis"),
    OYSTER("oyster"),
    FISH("fish"),
    SNAIL("snail");

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
        for(Category ctg : Category.values())
        {
            lookup.put(ctg.getName(), ctg);
        }
    }

    public static Category get(String text)
    {
        try {
            Category val = lookup.get(text);
            if(val == null){
                return CRAB;
            }
            return val;
        } catch (Exception ex) {
            ex.printStackTrace();
            return CRAB;
        }
    }

    @Override
    public String toString() {
        return this.name;
    }
}
