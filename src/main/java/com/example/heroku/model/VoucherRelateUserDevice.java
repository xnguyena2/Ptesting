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
@Table(name="voucher_relate_user_device")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherRelateUserDevice extends BaseEntity {

    @Id
    String id;

    private String voucher_second_id;

    private String device_id;

    private int reuse;

    public VoucherRelateUserDevice ResetID() {
        this.id = null;
        return this;
    }

    public VoucherRelateUserDevice ComsumeVoucher(){
        this.reuse--;
        return this;
    }

    public VoucherRelateUserDevice ForceSetupReuse(int reuse){
        this.reuse = reuse;
        return this;
    }

    public VoucherRelateUserDevice AutoFill(){
        this.createat = new Timestamp(new Date().getTime());
        return this;
    }
}
