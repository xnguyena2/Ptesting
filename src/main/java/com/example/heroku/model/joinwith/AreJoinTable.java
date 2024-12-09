package com.example.heroku.model.joinwith;

import com.example.heroku.model.Area;
import com.example.heroku.model.TableDetail;
import com.example.heroku.response.AreaData;
import com.example.heroku.response.TableDetailData;
import com.example.heroku.status.ActiveStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AreJoinTable extends Area {

    protected Long child_id;
    protected String child_group_id;
    protected Timestamp child_createat;
    private String child_area_id;
    private String child_table_id;
    private String child_package_second_id;
    private String child_table_name;
    private String child_detail;
    private ActiveStatus child_status;

    private float child_price;

    public String getID() {
        return getArea_id();
    }

    public Area getParent() {
        return super.toBuilder().build();
    }

    public TableDetailData getChild() {
        TableDetail tableDetail = TableDetail.builder()
                .id(getChild_id())
                .group_id(getChild_group_id())
                .createat(getChild_createat())
                .area_id(getChild_area_id())
                .table_id(getChild_table_id())
                .package_second_id(getChild_package_second_id())
                .table_name(getChild_table_name())
                .detail(getChild_detail())
                .status(getChild_status())
                .build();
        TableDetailData result = new TableDetailData(tableDetail);
        result.setPrice(getChild_price());
        if(result.getPrice() == 0) {
            result.setPrice(-1.0f);
        }
        return result;
    }

    public static AreaData GenerateAreaData(List<AreJoinTable> listData) {
        if (listData.isEmpty()) {
            return new AreaData();
        }
        AreJoinTable firstElement = listData.get(0);
        AreaData result = new AreaData(firstElement.getParent());
        List<TableDetailData> tableDetailDataList = new ArrayList<>();
        for (AreJoinTable child : listData) {
            TableDetailData tableDetail = child.getChild();
            if (tableDetail.getTable_id() == null || tableDetail.getTable_id().isEmpty()) {
                continue;
            }
            tableDetailDataList.add(tableDetail);
        }
        result.setListTable(tableDetailDataList);
        return result;
    }
}
