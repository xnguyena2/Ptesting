package com.example.heroku.model;

import io.r2dbc.postgresql.codec.Json;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="device_config")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceConfig {
    @Id
    String id;

    private String color;
}
