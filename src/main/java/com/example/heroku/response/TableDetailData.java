package com.example.heroku.response;

import com.example.heroku.model.TableDetail;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableDetailData extends TableDetail {

    private float price;

    public TableDetailData(TableDetail tableDetail) {
        price = -1;
        super.copy(tableDetail);
    }

    public TableDetailData setPrice(float price) {
        this.price = price;
        return this;
    }
}
