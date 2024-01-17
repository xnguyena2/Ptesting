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
@Table(name="delete_request")
@SuperBuilder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteRequest extends BaseEntity {

    private String delete_request_id;

    private String user_id;

    private ActiveStatus status;

    public DeleteRequest AutoFill() {
        super.AutoFill();
        if (this.delete_request_id == null || this.delete_request_id.isEmpty())
            this.delete_request_id = Util.getInstance().GenerateID();
        if (this.status == null)
            this.status = ActiveStatus.ACTIVE;
        return this;
    }

    public DeleteRequest AutoFill(String groupID) {
        AutoFill();
        group_id = groupID;
        return this;
    }
}
