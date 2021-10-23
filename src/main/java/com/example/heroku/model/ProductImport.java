package com.example.heroku.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name="product_import")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImport {

    @Id
    String id;

    private String product_import_second_id;

    private String product_id;

    private String product_name;

    private float price;

    private float amount;

    private String detail;

    private Timestamp createat;


    public ProductImport AutoFill() {
        this.createat = new Timestamp(new Date().getTime());
        return this;
    }
}
