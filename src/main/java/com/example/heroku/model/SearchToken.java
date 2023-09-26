package com.example.heroku.model;

import com.example.heroku.model.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="search_token")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SearchToken extends BaseEntity {

    private String product_second_id;
}
