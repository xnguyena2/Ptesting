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
@Table(name="beer_view_count")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BeerViewCount extends BaseEntity {

    @Id
    String id;

    private String beer_id;

    private String device_id;
}
