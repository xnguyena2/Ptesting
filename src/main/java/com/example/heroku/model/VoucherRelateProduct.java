package com.example.heroku.model;

import com.example.heroku.model.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="voucher_relate_product")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherRelateProduct extends BaseEntity {

    private String voucher_second_id;

    private String product_second_id;

    public VoucherRelateProduct AutoFill() {
        return (VoucherRelateProduct) super.AutoFill();
    }
}
