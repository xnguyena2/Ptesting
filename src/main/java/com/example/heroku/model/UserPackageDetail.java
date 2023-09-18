package com.example.heroku.model;

import com.example.heroku.util.Util;
import entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Table;
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

    @Id
    String id;

    private String package_second_id;

    private String device_id;

    private String package_type;

    private String voucher;

    private float price;

    private float discount_amount;

    private float discount_percent;

    private float ship_price;

    private String note;

    private String image;

    private String progress;

    private Status status;

    public static UserPackageDetail clone(UserPackageDetail s) {
        return s.toBuilder().build();
    }

    public void copy(UserPackageDetail s) {
        group_id = s.group_id;
        createat = s.createat;
        package_second_id = s.package_second_id;
        device_id = s.device_id;
        package_type = s.package_type;
        voucher = s.voucher;
        price = s.price;
        discount_amount = s.discount_amount;
        discount_percent = s.discount_percent;
        ship_price = s.ship_price;
        note = s.note;
        image = s.image;
        progress = s.progress;
        status = s.status;
    }

    public UserPackageDetail AutoFill() {
        if(package_second_id == null) {
            package_second_id = Util.getInstance().GenerateID();
        }
        return (UserPackageDetail) super.AutoFillIfNull();
    }


    public enum Status {
        CREATE("CREATE"),
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