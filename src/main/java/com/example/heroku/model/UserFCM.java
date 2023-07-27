package com.example.heroku.model;

import entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="user_fcm")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserFCM extends BaseEntity {

    @Id
    private String id;

    private String device_id;

    private String fcm_id;

    private Status status;

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
