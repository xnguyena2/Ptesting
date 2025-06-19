package com.example.heroku.model;

import com.example.heroku.model.entity.BaseEntity;
import com.example.heroku.status.ActiveStatus;
import com.example.heroku.util.Util;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name="tokens")
@SuperBuilder
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Tokens extends BaseEntity {

    private String token_second_id;

    private String token;

    private String username;

    private long expire;

    private ActiveStatus status;

    public Tokens AutoFill() {
        super.AutoFill();
        if (this.token_second_id == null || this.token_second_id.isEmpty())
            this.token_second_id = Util.getInstance().GenerateID();
        return this;
    }

    public Tokens AutoFill(String groupID) {
        AutoFill();
        setGroup_id(groupID);
        return this;
    }
}
