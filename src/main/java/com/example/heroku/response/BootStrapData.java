package com.example.heroku.response;

import com.example.heroku.model.DeviceConfig;
import com.example.heroku.model.ProductSerial;
import com.example.heroku.model.Store;
import com.example.heroku.model.statistics.BenifitByMonth;
import com.example.heroku.request.beer.BeerSubmitData;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder(toBuilder = true)
public class BootStrapData {
    private DeviceConfig deviceConfig;

    private Store store;

    private BenifitByMonth benifit;

    private List<String> carousel;

    private List<BeerSubmitData> products;

    private List<ProductSerial> productSerials;
}
