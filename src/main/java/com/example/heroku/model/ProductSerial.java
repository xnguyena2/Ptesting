package com.example.heroku.model;

import com.example.heroku.model.entity.BaseEntity;
import com.example.heroku.util.Util;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "product_serial")
public class ProductSerial extends BaseEntity {

    private String product_serial_id;
    private String product_second_id;
    private String product_unit_second_id;
    private String group_import_second_id;
    private String package_second_id;
    private String warranty;
    private String note;

    private Status status;

    public ProductSerial autoFill() {
        super.AutoFill();
        if (this.product_serial_id == null || this.product_serial_id.isEmpty()) {
            this.product_serial_id = Util.getInstance().GenerateID();
        }
        return this;
    }

    public ProductSerial autoFill(String group_id) {
        this.setGroup_id(group_id);
        return this.autoFill();
    }

    @Getter
    public enum Status {

        SOLD("SOLD"),
        EXPORT("EXPORT");

        private final String name;

        Status(String name) {
            this.name = name;
        }

        private static final Map<String, Status> LOOKUP;

        static {
            Map<String, Status> map = new HashMap<>();
            for (Status status : values()) {
                map.put(status.getName(), status);
            }
            LOOKUP = Collections.unmodifiableMap(map);
        }

        public static Status get(String text) {
            return LOOKUP.getOrDefault(text, null);
        }

        @Override
        public String toString() {
            return name;
        }
    }

}
