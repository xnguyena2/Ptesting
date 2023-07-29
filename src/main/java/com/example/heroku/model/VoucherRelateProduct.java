package com.example.heroku.model;

import entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="voucher_relate_product")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherRelateProduct extends BaseEntity {

    @Id
    String id;

    private String voucher_second_id;

    private String product_second_id;

    public VoucherRelateProduct AutoFill(){
        this.createat = new Timestamp(new Date().getTime());
        return this;
    }
}
