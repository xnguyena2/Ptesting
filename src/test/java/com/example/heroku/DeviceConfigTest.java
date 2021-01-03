package com.example.heroku;

import com.example.heroku.model.Beer;
import com.example.heroku.services.ClientDevice;
import com.example.heroku.services.DeviceConfig;
import lombok.Builder;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class DeviceConfigTest {
    private DeviceConfig deviceConfig;

    private ClientDevice clientDevice;

    public void DeviceConfigTest() {
        this.deviceConfig
                .UpdateConfig(com.example.heroku.model.DeviceConfig.builder().color("#ffffff").build())
                .then(this.deviceConfig.GetConfig())
                .as(StepVerifier::create)
                .consumeNextWith(config -> {
                            assertThat(config.getColor()).isEqualTo("#ffffff");
                        }
                )
                .verifyComplete();

        this.clientDevice.bootStrapData()
                .as(StepVerifier::create)
                .consumeNextWith(config -> {
                            assertThat(config.getDeviceConfig().getColor()).isEqualTo("#ffffff");
                            assertThat(config.getListCarousel()).isEqualTo(new ArrayList<String>() {{
                                add("1");
                                add("2");
                                add("3");
                                add("4");
                            }});
                        }
                )
                .verifyComplete();
    }
}
