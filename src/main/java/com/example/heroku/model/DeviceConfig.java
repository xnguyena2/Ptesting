package com.example.heroku.model;

import entity.BaseEntity;
import io.r2dbc.postgresql.codec.Json;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Table;


@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="device_config")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceConfig extends BaseEntity {
    @Id
    String id;

    private String color;
}
