package com.example.heroku.model;

import com.example.heroku.model.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="beer_view_count")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BeerViewCount extends BaseEntity {

    private String beer_id;

    private String device_id;
}
