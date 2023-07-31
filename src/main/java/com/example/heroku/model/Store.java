package com.example.heroku.model;

import entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="store")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Store extends BaseEntity {

    @Id
    String id;


    private String name;

    private Status status;


    public enum Status {
        OPEN,
        CLOSE
    }
}
