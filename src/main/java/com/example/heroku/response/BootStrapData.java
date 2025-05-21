package com.example.heroku.response;

import com.example.heroku.model.DeviceConfig;
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

    public BootStrapData setStore(Store store) {
        this.store = store;
        return this;
    }

    public BootStrapData setDeviceConfig(DeviceConfig deviceConfig) {
        this.deviceConfig = deviceConfig;
        return this;
    }

    public BootStrapData setBenifit(BenifitByMonth b){
        benifit = b;
        return this;
    }
}
