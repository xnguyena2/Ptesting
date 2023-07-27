package com.example.heroku.model;

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
@Table(name="user_device")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserDevice extends BaseEntity {

    @Id
    String id;

    private String device_id;

    private String user_first_name;

    private String user_last_name;

    private Status status;

    public UserDevice AutoFill() {
        this.createat = new Timestamp(new Date().getTime());
        return this;
    }


    public enum Status {
        ACTIVE("ACTIVE"),
        BLOCK("BLOCK");


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
