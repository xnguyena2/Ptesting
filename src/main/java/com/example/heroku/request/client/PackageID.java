package com.example.heroku.request.client;

import com.example.heroku.model.UserPackageDetail;
import com.example.heroku.request.client.base.BaseID;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PackageID extends BaseID {
    private String device_id;
    private String package_id;
    private LocalDateTime from;
    private LocalDateTime to;
    private UserPackageDetail.Status status;
}