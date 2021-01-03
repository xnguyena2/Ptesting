package com.example.heroku.response;

import com.example.heroku.model.DeviceConfig;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BootStrapData {
    private DeviceConfig deviceConfig;

    private List<String> listCarousel;

    public BootStrapData setDeviceConfig(DeviceConfig deviceConfig) {
        this.deviceConfig = deviceConfig;
        return this;
    }
}
