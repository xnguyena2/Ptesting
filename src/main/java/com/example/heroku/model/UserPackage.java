package com.example.heroku.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name="user_package")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPackage {

    @Id
    String id;

    private String device_id;

    private String product_second_id;

    private String product_unit_second_id;

    private int number_unit;

    private Status status;

    private Timestamp createat;

    public UserPackage(UserPackage s){
        this.id = s.id;
        this.device_id = s.device_id;
        this.product_second_id = s.product_second_id;
        this.product_unit_second_id = s.product_unit_second_id;
        this.number_unit = s.number_unit;
        this.status = s.status;
        this.createat = s.createat;
    }

    public enum Status{

    }

}
