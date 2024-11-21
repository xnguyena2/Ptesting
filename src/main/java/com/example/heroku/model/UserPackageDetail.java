package com.example.heroku.model;

import com.example.heroku.util.Util;
import com.example.heroku.model.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="user_package_detail")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserPackageDetail  extends BaseEntity {

    private String package_second_id;

    private String device_id;

    private String staff_id;

    private String staff_name;

    private String package_type;

    private String table_id;

    private String table_name;

    private String area_id;

    private String area_name;

    private String voucher;

    private float price;

    private float payment;

    private float discount_amount;

    private float discount_percent;

    private float discount_promotional;

    private float discount_by_point;

    private float additional_fee;

    private String additional_config;

    private float ship_price;

    private float deliver_ship_price;

    private float cost;

    private float profit;

    private int point;

    private String note;

    private String image;

    private String progress;

    private String meta_search;

    private Status status;

//    update UserPackageDetailJoinWithUserPackage

    public static UserPackageDetail clone(UserPackageDetail s) {
        return s.toBuilder().build();
    }

    public void copy(UserPackageDetail s) {
        id = s.id;
        group_id = s.group_id;
        createat = s.createat;
        package_second_id = s.package_second_id;
        device_id = s.device_id;
        staff_id = s.staff_id;
        staff_name = s.staff_name;
        package_type = s.package_type;
        area_id = s.area_id;
        area_name = s.area_name;
        table_id = s.table_id;
        table_name = s.table_name;
        voucher = s.voucher;
        price = s.price;
        payment = s.payment;
        discount_amount = s.discount_amount;
        discount_percent = s.discount_percent;
        discount_promotional = s.discount_promotional;
        discount_by_point = s.discount_by_point;
        additional_fee = s.additional_fee;
        additional_config = s.additional_config;
        ship_price = s.ship_price;
        deliver_ship_price = s.deliver_ship_price;
        cost = s.cost;
        profit = s.profit;
        point = s.point;
        note = s.note;
        image = s.image;
        progress = s.progress;
        meta_search = s.meta_search;
        status = s.status;
    }

    public float GetDiscountValue() {
        return discount_amount + (discount_percent / 100) * price;
    }

    public UserPackageDetail AutoFill() {
        if(package_second_id == null || package_second_id.isEmpty()) {
            package_second_id = Util.getInstance().GenerateID();
        }
        return (UserPackageDetail) super.AutoFillIfNull();
    }


    public enum Status {
        CREATE("CREATE"),
        CANCEL("CANCEL"),
        RETURN("RETURN"),
        DONE("DONE"),

        WEB_TEMP("WEB_TEMP"),
        WEB_SUBMIT("WEB_SUBMIT");


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
                    return CREATE;
                }
                return val;
            } catch (Exception ex) {
                ex.printStackTrace();
                return CREATE;
            }
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

}