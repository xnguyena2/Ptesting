package com.example.heroku.model;

import com.example.heroku.model.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="store")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Store extends BaseEntity {

    private String name;
    private String time_open;
    private String address;
    private String phone;

    private Status status;

    public Store AutoFill(String groupID) {
        super.AutoFill();
        this.group_id = groupID;
        if (status == null) {
            status = Status.ACTIVE;
        }
        return this;
    }

    public enum Status {
        ACTIVE("ACTIVE"),
        CLOSE("CLOSE");


        private final String name;

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
                    return ACTIVE;
                }
                return val;
            } catch (Exception ex) {
                ex.printStackTrace();
                return ACTIVE;
            }
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
