package com.example.heroku.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="shipping_provider")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingProvider {
    @Id
    String id;

    private String provider_id;
    private String name;
    private String config;
}
