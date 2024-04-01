package com.example.heroku.request.warehouse;

import com.example.heroku.request.client.base.BaseID;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SearchImportQuery extends BaseID {


    private String product_second_id;
    private String product_unit_second_id;
    private Timestamp from;
    private Timestamp to;

}
