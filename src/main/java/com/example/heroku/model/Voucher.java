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
@Table(name="voucher")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Voucher {

    @Id
    String id;

    private String voucher_second_id;

    private String detail;

    private float discount;

    private float amount;

    private int reuse;

    private Timestamp date_expire;

    private Status status;

    private Timestamp createat;

    private boolean for_all_beer;

    private boolean for_all_user;

    public Voucher AutoFill(){
        this.createat = new Timestamp(new Date().getTime());
        return this;
    }

    public enum Status{

    }

}
