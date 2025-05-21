package com.example.heroku.model;

import com.example.heroku.model.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="user_pay_sodi")
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserPaySoDi extends BaseEntity {

    private float amount;

    private String note;

    private String plan;

    private int bonus;

    public UserPaySoDi AutoFill(){
        super.AutoFill();
        return this;
    }
}
