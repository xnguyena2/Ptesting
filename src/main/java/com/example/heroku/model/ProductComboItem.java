package com.example.heroku.model;

import com.example.heroku.model.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="product_combo_item")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductComboItem extends BaseEntity {

    private String product_second_id;

    private String product_unit_second_id;

    private String item_product_second_id;

    private String item_product_unit_second_id;

    private float unit_number;

    public ProductComboItem AutoFill(String groupID, String product_second_id) {
        setGroup_id(groupID);
        setProduct_second_id(product_second_id);
        super.AutoFill();
        return this;
    }
}
