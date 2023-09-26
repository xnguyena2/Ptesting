package com.example.heroku.model;

import com.example.heroku.model.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="voucher_relate_user_device")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherRelateUserDevice extends BaseEntity {

    private String voucher_second_id;

    private String device_id;

    private int reuse;

    public VoucherRelateUserDevice AutoFill(){
        return (VoucherRelateUserDevice) super.AutoFill();
    }

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

}
