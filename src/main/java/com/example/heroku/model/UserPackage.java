package com.example.heroku.model;

import entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="user_package")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserPackage extends BaseEntity {

    @Id
    String id;

    private String device_id;

    private String product_second_id;

    private String product_unit_second_id;

    private int number_unit;

    private Status status;

    public UserPackage(UserPackage s){
        this.id = s.id;
        this.group_id = s.group_id;
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
