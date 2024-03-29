package com.example.heroku.model;

import com.example.heroku.model.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="user_address")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserAddress extends BaseEntity {

    private String address_id;

    private String device_id;

    private String reciver_fullname;

    private String phone_number;

    private String house_number;

    private int region;

    private int district;

    private int ward;

    private Status status;

    public UserAddress changeStatus(Status status){
        this.setStatus(status);
        return this;
    }

    public UserAddress AutoFill() {
        return (UserAddress) super.AutoFill();
    }

    public UserAddress Clean() {
        this.setId(null);
        AutoFill();
        return this;
    }


    public enum Status{
        FAIL,
        SUCCESS
    }
}
