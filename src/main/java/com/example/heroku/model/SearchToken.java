package com.example.heroku.model;

import entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

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
    @Id
    String id;

    private String product_second_id;
}
