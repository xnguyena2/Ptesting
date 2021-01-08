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
@Table(name="voucher_relate_beer")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherRelateBeer {

    @Id
    String id;

    private int voucher_id;

    private String beer_id;

    private Timestamp createat;
}
