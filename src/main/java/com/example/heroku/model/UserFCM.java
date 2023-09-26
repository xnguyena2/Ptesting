package com.example.heroku.model;

import com.example.heroku.model.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="user_fcm")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserFCM extends BaseEntity {

    private String device_id;

    private String fcm_id;

    private Status status;

    public UserFCM AutoFill(){
        super.AutoFill();
        this.status = Status.ACTIVE;
        return this;
    }

    public enum Status {
        ACTIVE,
        BLOCK
    }
}
