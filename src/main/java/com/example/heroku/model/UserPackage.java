package com.example.heroku.model;

import com.example.heroku.model.entity.BaseEntity;
import com.example.heroku.util.Util;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="user_package")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserPackage extends BaseEntity {

    private String package_second_id;

    private String device_id;

    private String product_second_id;

    private String product_unit_second_id;

    private String product_name;

    private String product_unit_name;

    private String product_group_unit_name;

    private float number_services_unit;

    private float number_unit;

    private float price;

    private float buy_price;

    private float discount_amount;

    private float discount_percent;

    private float discount_promotional;

    private String note;

    private UserPackageDetail.Status status;

//    update UserPackageDetailJoinWithUserPackage

    public UserPackage(UserPackage s) {
        super(s);
        this.setId(s.getId());
        this.package_second_id = s.package_second_id;
        this.device_id = s.device_id;
        this.product_second_id = s.product_second_id;
        this.product_unit_second_id = s.product_unit_second_id;
        this.product_name = s.product_name;
        this.product_unit_name = s.product_unit_name;
        this.product_group_unit_name = s.product_group_unit_name;
        this.number_services_unit = s.number_services_unit;
        this.number_unit = s.number_unit;
        this.price = s.price;
        this.buy_price = s.buy_price;
        this.discount_amount = s.discount_amount;
        this.discount_percent = s.discount_percent;
        this.discount_promotional = s.discount_promotional;
        this.note = s.note;
        this.status = s.status;
    }

    public UserPackage AutoFill() {
        if (status == null) {
            status = UserPackageDetail.Status.CREATE;
        }
        super.AutoFillIfNull();
        return this;
    }

}
