package com.example.heroku.model;

import com.example.heroku.model.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="shipping_provider")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingProvider extends BaseEntity {

    private String provider_id;
    private String name;
    private String config;
}
