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
@Table(name="IMAGE")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Image extends BaseEntity {
    @Id
    String id;

    private String imgid;

    private  String tag;

    private String thumbnail;

    private String medium;

    private String large;

    private String category;
}
