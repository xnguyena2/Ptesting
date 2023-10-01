package com.example.heroku.response;

import com.example.heroku.model.TableDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableDetailData extends TableDetail {

    private float price;

    public TableDetailData(TableDetail tableDetail) {
        super.copy(tableDetail);
    }

    public TableDetailData setPrice(float price) {
        this.price = price;
        return this;
    }
}
