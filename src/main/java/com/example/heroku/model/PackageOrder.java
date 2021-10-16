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
@Table(name="package_order")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackageOrder {

    @Id
    String id;

    private String package_order_second_id;

    private String user_device_id;

    private String reciver_address;

    private int region_id;

    private int district_id;

    private int ward_id;

    private String reciver_fullname;

    private String phone_number;

    private float total_price;

    private float ship_price;

    private Status status;

    private Timestamp createat;

    public PackageOrder AutoFill(boolean isPreOrder) {
        if (this.package_order_second_id == null || this.package_order_second_id.equals(""))
            this.package_order_second_id = Util.getInstance().GenerateID();
        this.createat = new Timestamp(new Date().getTime());
        this.status = isPreOrder ? Status.PRE_ORDER : Status.ORDER;
        return this;
    }

    public enum Status{
        PRE_ORDER("PRE_ORDER"),
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
                    return PRE_ORDER;
                }
                return val;
            } catch (Exception ex) {
                ex.printStackTrace();
                return PRE_ORDER;
            }
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
