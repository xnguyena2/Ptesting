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
@Table(name="map_key_value")
@SuperBuilder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MapKeyValue extends BaseEntity {

    private String id_o;

    private String value_o;

    public MapKeyValue AutoFill() {
        super.AutoFill();
        return this;
    }

    public MapKeyValue AutoFill(String groupID) {
        AutoFill();
        group_id = groupID;
        return this;
    }
}
