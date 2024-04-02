package com.example.heroku.model;

import com.example.heroku.model.entity.BaseEntity;
import com.example.heroku.util.Util;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="group_import")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class GroupImport extends BaseEntity {

    private String group_import_second_id;

    private String supplier_id;

    private float total_price;

    private int total_amount;

    private float payment;

    private float discount_amount;

    private float additional_fee;

    private String note;

    private String images;

    private ProductImport.ImportType type;

    private ProductImport.Status status;

    // GroupImportJoinProductImport

    public void copy(GroupImport g) {
        super.copy(g);
        group_import_second_id = g.group_import_second_id;
        supplier_id = g.supplier_id;
        total_price = g.total_price;
        total_amount = g.total_amount;
        payment = g.payment;
        discount_amount = g.discount_amount;
        additional_fee = g.additional_fee;
        note = g.note;
        images = g.images;
        type = g.type;
        status = g.status;
    }


    public GroupImport AutoFill() {
        if (group_import_second_id == null || group_import_second_id.isEmpty()) {
            group_import_second_id = Util.getInstance().GenerateID();
        }
        if (status == null) {
            status = ProductImport.Status.CREATE;
        }
        if (type == null) {
            type = ProductImport.ImportType.UN_KNOW;
        }
        return (GroupImport) super.AutoFill();
    }
}
