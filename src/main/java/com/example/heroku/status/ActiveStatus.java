package com.example.heroku.status;


import java.util.HashMap;
import java.util.Map;

public enum ActiveStatus {

    ACTIVE("ACTIVE"),
    DE_ACTIVE("DE_ACTIVE"),
    ;


    private String name;

    ActiveStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private static final Map<String, ActiveStatus> lookup = new HashMap<>();

    static {
        for (ActiveStatus sts : ActiveStatus.values()) {
            lookup.put(sts.getName(), sts);
        }
    }

    public static ActiveStatus get(String text) {
        try {
            ActiveStatus val = lookup.get(text);
            if (val == null) {
                return DE_ACTIVE;
            }
            return val;
        } catch (Exception ex) {
            ex.printStackTrace();
            return DE_ACTIVE;
        }
    }

    @Override
    public String toString() {
        return this.name;
    }

}
