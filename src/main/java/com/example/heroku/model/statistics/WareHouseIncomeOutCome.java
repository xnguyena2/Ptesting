package com.example.heroku.model.statistics;

import lombok.Data;

@Data
public class WareHouseIncomeOutCome {
    private float export_price_inside;
    private float export_price_outside;

    private float import_price_inside;
    private float import_price_outside;

    private float export_amount_inside;
    private float export_amount_outside;

    private float import_amount_inside;
    private float import_amount_outside;
}
