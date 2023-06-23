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
@Table(name="user_address")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAddress {

    @Id
    String id;

    private String address_id;

    private String device_id;

    private String reciver_fullname;

    private String phone_number;

    private String house_number;

    private int region;

    private int district;

    private int ward;

    private Status status;

    private Timestamp createat;

    public UserAddress changeStatus(Status status){
        this.setStatus(status);
        return this;
    }

    public UserAddress AutoFill(){
        this.createat = new Timestamp(new Date().getTime());
        return this;
    }


    public enum Status{
        FAIL,
        SUCCESS
    }
}
