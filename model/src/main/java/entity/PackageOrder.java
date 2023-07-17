package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PackageOrder extends BaseEntity {

    protected String package_order_second_id;

    protected String user_device_id;

    protected String reciver_address;

    protected int region_id;

    protected int district_id;

    protected int ward_id;

    protected String reciver_fullname;

    protected String phone_number;

    protected String phone_number_clean;

    protected float total_price;

    protected float real_price;

    protected float ship_price;

    protected Status status;

    public enum Status{
        PRE_ORDER("PRE_ORDER"),
        ORDER("ORDER"),
        SENDING("SENDING"),
        CANCEL("CANCEL"),
        DONE("DONE");

        protected String name;

        Status(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }

        protected static final Map<String, Status> lookup = new HashMap<>();

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
