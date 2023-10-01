package com.example.heroku.response;

import com.example.heroku.model.Area;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AreaData extends Area {
    private List<TableDetailData> listTable;

    public AreaData AutoFill() {
        super.AutoFill();
        if(listTable == null) {
            return this;
        }
        for (TableDetailData tableDetailData:
             listTable) {
            tableDetailData.AutoFill(getGroup_id());
        }
        return this;
    }

    public AreaData(Area area) {
        super.copy(area);
        this.listTable = new ArrayList<>();
    }

    public AreaData AddTable(TableDetailData tableDetail) {
        if (this.listTable == null) {
            this.listTable = new ArrayList<>();
        }
        this.listTable.add(tableDetail);
        return this;
    }
}
