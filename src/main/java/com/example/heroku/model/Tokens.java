package com.example.heroku.model;

import com.example.heroku.model.entity.BaseEntity;
import com.example.heroku.status.ActiveStatus;
import com.example.heroku.util.Util;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Table(name="tokens")
@SuperBuilder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tokens extends BaseEntity {

    private String token_second_id;

    private String token;

    private ActiveStatus status;

    public Tokens AutoFill() {
        super.AutoFill();
        if (this.token_second_id == null || this.token_second_id.isEmpty())
            this.token_second_id = Util.getInstance().GenerateID();
        return this;
    }

    public Tokens AutoFill(String groupID) {
        AutoFill();
        group_id = groupID;
        return this;
    }
}
