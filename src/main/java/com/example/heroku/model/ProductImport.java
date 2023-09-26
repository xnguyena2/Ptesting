package com.example.heroku.model;

import com.example.heroku.model.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="product_import")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImport extends BaseEntity {

    private String product_import_second_id;

    private String product_id;

    private String product_name;

    private float price;

    private float amount;

    private String detail;


    public ProductImport AutoFill() {
        return (ProductImport) super.AutoFill();
    }
}
