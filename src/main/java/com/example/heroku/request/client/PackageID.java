package com.example.heroku.request.client;

import com.example.heroku.request.client.base.BaseID;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PackageID extends BaseID {
    private String device_id;
    private String package_id;
}