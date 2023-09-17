package com.example.heroku.model;

import com.example.heroku.util.Util;
import entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="product_import")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImport extends BaseEntity {

    @Id
    String id;

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
