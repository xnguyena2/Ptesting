package com.example.heroku.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Buyer {
    private String reciver_fullname;

    private String phone_number_clean;

    private String phone_number;

    private String reciver_address;

    private int region_id;

    private int district_id;

    private int ward_id;

    private float real_price;
}
