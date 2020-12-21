package com.example.heroku.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="BEER")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Beer {
    @Id
    @Column(name="ID")
    private String ID;

    @Column(name="Name")
    private String name;

    @Column(name="Detail")
    private String detail;

    @Column(name = "Discount")
    private float discount;

    @Column(name="DícountEX")
    private Date discountExpire;

    @Column(name="CreateAT")
    private Date createAt;
}
