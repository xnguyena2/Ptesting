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
@Table(name="voucher_relate_user_device")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherRelateUserDevice {

    @Id
    String id;

    private String voucher_second_id;

    private String device_id;

    private int reuse;

    private Timestamp createat;

    public VoucherRelateUserDevice ResetID() {
        this.id = null;
        return this;
    }

    public VoucherRelateUserDevice ComsumeVoucher(){
        this.reuse--;
        return this;
    }

    public VoucherRelateUserDevice AutoFill(){
        this.createat = new Timestamp(new Date().getTime());
        return this;
    }
}
