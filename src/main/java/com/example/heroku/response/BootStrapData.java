package com.example.heroku.response;

import com.example.heroku.model.DeviceConfig;
import com.example.heroku.model.statistics.BenifitByMonth;
import com.example.heroku.request.beer.BeerSubmitData;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BootStrapData {
    private DeviceConfig deviceConfig;

    private BenifitByMonth benifit;

    private List<String> carousel;

    private List<BeerSubmitData> products;

    public BootStrapData setDeviceConfig(DeviceConfig deviceConfig) {
        this.deviceConfig = deviceConfig;
        return this;
    }

    public BootStrapData setBenifit(BenifitByMonth b){
        benifit = b;
        return this;
    }
}
