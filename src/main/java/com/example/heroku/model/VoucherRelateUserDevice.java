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
@Table(name="voucher_relate_user_device")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherRelateUserDevice {

    @Id
    String id;

    private int voucher_id;

    private String user_device;

    private Timestamp createat;

}
