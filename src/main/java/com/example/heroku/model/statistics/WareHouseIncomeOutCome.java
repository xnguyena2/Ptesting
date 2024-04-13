package com.example.heroku.model.statistics;

import lombok.Data;

@Data
public class WareHouseIncomeOutCome {
    private float export_price_inside;
    private float export_price_outside;

    private float import_price_inside;
    private float import_price_outside;

    private int export_amount_inside;
    private int export_amount_outside;

    private int import_amount_inside;
    private int import_amount_outside;
}
