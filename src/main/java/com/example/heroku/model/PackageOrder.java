package com.example.heroku.model;

import com.example.heroku.util.Util;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name="package_order")
@EqualsAndHashCode(callSuper=false)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PackageOrder extends entity.PackageOrder {
    @Id
    String id;

    public PackageOrder AutoFill(boolean isPreOrder) {
        if (this.package_order_second_id == null || this.package_order_second_id.equals(""))
            this.package_order_second_id = Util.getInstance().GenerateID();
        this.createat = new Timestamp(new Date().getTime());
        this.status = isPreOrder ? Status.PRE_ORDER : Status.ORDER;
        this.phone_number_clean = this.phone_number.replaceAll("[^0-9+]", "").replace("+84", "0");
        System.out.println("Phone clean: " + this.phone_number_clean);
        return this;
    }
}
