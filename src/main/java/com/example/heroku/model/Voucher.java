package com.example.heroku.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name="voucher")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Voucher {

    @Id
    String id;

    private String detail;

    private float discount;

    private float amount;

    private Timestamp date_expire;

    private Status status;

    private Timestamp createat;


    public enum Status{

    }

}
