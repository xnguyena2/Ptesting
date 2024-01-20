package com.example.heroku.model;

import com.example.heroku.model.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="users_info")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UsersInfo extends BaseEntity {

    private String username;

    private String user_fullname;

    private String phone;

    private String title;

    private String roles;

    public UsersInfo AutoFill() {
        super.AutoFill();
        return this;
    }
}
