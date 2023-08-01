package com.example.heroku.model;

import entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Table(name="buyer")
@SuperBuilder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Buyer extends BaseEntity {

    @Id
    String id;

    private String device_id;

    private String reciver_fullname;

    private String phone_number_clean;

    private String phone_number;

    private String reciver_address;

    private int region_id;

    private int district_id;

    private int ward_id;

    private float real_price;

    private float total_price;

    private float ship_price;

    private String status;
}
