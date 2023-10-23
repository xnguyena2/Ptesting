package com.example.heroku.model;

import com.example.heroku.model.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

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

    private String color;

    private String categorys;

    private String config;
}
