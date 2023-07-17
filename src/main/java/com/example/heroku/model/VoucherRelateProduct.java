package com.example.heroku.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name="voucher_relate_product")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherRelateProduct {

    @Id
    String id;

    private String voucher_second_id;

    private String beer_second_id;

    private Timestamp createat;

    public VoucherRelateProduct AutoFill(){
        this.createat = new Timestamp(new Date().getTime());
        return this;
    }
}
