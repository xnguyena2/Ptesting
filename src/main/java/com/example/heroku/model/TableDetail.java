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
@Table(name="table_detail")
@SuperBuilder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableDetail extends BaseEntity {

    private String area_id;

    private String table_id;

    private String package_second_id;

    private String table_name;

    private String detail;

    private ActiveStatus status;

    public void copy(TableDetail t) {
        id = t.id;
        group_id = t.group_id;
        createat = t.createat;
        area_id = t.area_id;
        table_id = t.table_id;
        package_second_id = t.package_second_id;
        table_name = t.table_name;
        detail = t.detail;
        status = t.status;
    }

    public TableDetail AutoFill() {
        super.AutoFill();
        if (this.table_id == null || this.table_id.isEmpty())
            this.table_id = Util.getInstance().GenerateID();
        return this;
    }

    public TableDetail AutoFill(String groupID) {
        AutoFill();
        group_id = groupID;
        return this;
    }
}
