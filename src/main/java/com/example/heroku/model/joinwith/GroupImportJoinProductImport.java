package com.example.heroku.model.joinwith;

import com.example.heroku.model.GroupImport;
import com.example.heroku.model.ProductImport;
import com.example.heroku.request.warehouse.GroupImportWithItem;
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
public class GroupImportJoinProductImport extends GroupImport {


    // ProductImport


    protected Long child_id;
    protected String child_group_id;
    protected Timestamp child_createat;
    private String child_group_import_second_id;
    private String child_product_second_id;
    private String child_product_unit_second_id;
    private String child_product_unit_name_category;
    private float child_price;
    private float child_amount;
    private String child_note;
    private ProductImport.ImportType child_type;
    private ProductImport.Status child_status;


    public String getID() {
        return getGroup_import_second_id();
    }

    public GroupImport getParent() {
        return super.toBuilder().build();
    }

    public ProductImport getChild() {
        return ProductImport.builder()
                .id(child_id)
                .group_id(child_group_id)
                .createat(child_createat)
                .group_import_second_id(child_group_import_second_id)
                .product_second_id(child_product_second_id)
                .product_unit_second_id(child_product_unit_second_id)
                .product_unit_name_category(child_product_unit_name_category)
                .price(child_price)
                .amount(child_amount)
                .note(child_note)
                .type(child_type)
                .status(child_status)
                .build();
    }

    public static GroupImportWithItem GenerateGroupImportWithItem(List<GroupImportJoinProductImport> listData) {
        if (listData.isEmpty()) {
            return GroupImportWithItem.Empty();
        }
        GroupImportJoinProductImport firstElement = listData.get(0);
        GroupImportWithItem result = GroupImportWithItem.FromGroupImport(firstElement.getParent());
        List<ProductImport> productUnitList = new ArrayList<ProductImport>();
        for (GroupImportJoinProductImport child : listData) {
            ProductImport productUnit = child.getChild();
            if (productUnit.getGroup_import_second_id() == null || productUnit.getGroup_import_second_id().isEmpty()) {
                continue;
            }
            productUnitList.add(productUnit);
        }
        result.setItems(productUnitList.toArray(new ProductImport[0]));
        return result;
    }
}
