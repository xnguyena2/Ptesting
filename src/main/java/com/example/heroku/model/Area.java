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
@Table(name="area")
@SuperBuilder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Area extends BaseEntity {

    private String area_id;

    private String area_name;

    private String detail;

    private String meta_search;

    private ActiveStatus status;

    public void copy(Area a) {
        id = a.id;
        group_id = a.group_id;
        createat = a.createat;
        area_id = a.area_id;
        area_name = a.area_name;
        detail = a.detail;
        meta_search = a.meta_search;
        status = a.status;
    }

    public String getTokens() {
        if (area_name == null && detail == null)
            return "";
        if (area_name == null)
            return Util.getInstance().RemoveAccent(detail);
        if (detail == null)
            return Util.getInstance().RemoveAccent(area_name);
        return Util.getInstance().RemoveAccent(area_name + " " + detail);
    }

    public Area UpdateMetaSearch() {
        this.meta_search = this.getTokens();
        return this;
    }

    public Area AutoFill() {
        super.AutoFill();
        if (this.area_id == null || this.area_id.isEmpty())
            this.area_id = Util.getInstance().GenerateID();
        return this.UpdateMetaSearch();
    }

    public Area AutoFill(String groupID) {
        AutoFill();
        group_id = groupID;
        return this;
    }
}
