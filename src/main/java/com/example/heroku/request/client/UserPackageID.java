package com.example.heroku.request.client;

import com.example.heroku.model.UserPackageDetail;
import com.example.heroku.request.client.base.BaseID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserPackageID extends BaseID {
    private String product_second_id;
    private String product_unit_second_id;
    private long after_id;
    private Timestamp from;
    private Timestamp to;
    private UserPackageDetail.Status status;
}