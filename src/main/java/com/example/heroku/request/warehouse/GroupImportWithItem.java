package com.example.heroku.request.warehouse;

import com.example.heroku.model.GroupImport;
import com.example.heroku.model.ProductImport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder()
@NoArgsConstructor
@AllArgsConstructor
public class GroupImportWithItem extends GroupImport {
    private ProductImport[] items;

    public static GroupImportWithItem FromGroupImport(GroupImport groupImport){
        GroupImportWithItem obj = GroupImportWithItem.builder().build();
        obj.copy(groupImport);;
        return obj;
    }

    public static GroupImportWithItem Empty() {
        return GroupImportWithItem.builder().build();
    }

    public boolean isEmpty() {
        return items == null;
    }

    public ProductImport[] getProductImport() {
        if(items == null)
            return null;
        for (ProductImport productImport :
                items) {
            productImport.setGroup_import_second_id(getGroup_import_second_id());
            productImport.setGroup_id(getGroup_id());
            productImport.setStatus(getStatus());
            productImport.setType(getType());
            productImport.AutoFill();
        }
        return items;
    }
}
