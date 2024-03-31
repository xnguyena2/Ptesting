package com.example.heroku.model.entity;

import com.example.heroku.util.Util;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.sql.Timestamp;

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

    public void copy(BaseEntity b){
        id = b.id;
        group_id = b.group_id;
        createat = b.createat;
    }

    public BaseEntity AutoFill() {
        this.createat = Util.getInstance().Now();
        return this;
    }

    public BaseEntity AutoFillIfNull() {
        if(this.createat == null) {
            this.createat = Util.getInstance().Now();
        }
        return this;
    }
}
