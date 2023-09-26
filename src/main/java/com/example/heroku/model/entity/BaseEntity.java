package com.example.heroku.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import org.springframework.data.annotation.Id;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    protected Long id;


    protected String group_id;
    protected Timestamp createat;

    public BaseEntity(BaseEntity s) {
        group_id = s.getGroup_id();
        createat = s.getCreateat();
    }

    public BaseEntity AutoFill() {
        this.createat = new Timestamp(new Date().getTime());
        return this;
    }

    public BaseEntity AutoFillIfNull() {
        if(this.createat == null) {
            this.createat = new Timestamp(new Date().getTime());
        }
        return this;
    }
}
