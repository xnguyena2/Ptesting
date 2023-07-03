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
@Table(name="user_fcm")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFCM {

    @Id
    private String id;

    private String device_id;

    private String fcm_id;

    private Status status;

    private Timestamp createat;

    public UserFCM AutoFill(){
        this.createat = new Timestamp(new Date().getTime());
        this.status = Status.ACTIVE;
        return this;
    }

    public enum Status {
        ACTIVE,
        BLOCK
    }
}
