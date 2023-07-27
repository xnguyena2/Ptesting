package com.example.heroku.model;

import entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

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
    @Id
    String id;

    private String provider_id;
    private String name;
    private String config;
}
