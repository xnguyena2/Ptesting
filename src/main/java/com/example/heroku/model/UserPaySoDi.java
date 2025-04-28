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
@RequiredArgsConstructor
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserPaySoDi extends BaseEntity {

    private String device_id;

    private String fcm_id;

    private Status status;

    public UserPaySoDi AutoFill(){
        super.AutoFill();
        this.status = Status.ACTIVE;
        return this;
    }

    public enum Status {
        ACTIVE,
        BLOCK
    }
}
