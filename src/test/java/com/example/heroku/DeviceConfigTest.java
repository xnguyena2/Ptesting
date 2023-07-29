package com.example.heroku;

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

    String group;

    public void DeviceConfigTestWithoutImage() {
        this.deviceConfig
                .UpdateConfig(com.example.heroku.model.DeviceConfig.builder().color("#ffffff").group_id(group).build())
                .block();
        this.deviceConfig.GetConfig(group)
                .as(StepVerifier::create)
                .consumeNextWith(config -> {
                            assertThat(config.getColor()).isEqualTo("#ffffff");
                        }
                )
                .verifyComplete();

        this.deviceConfig
                .UpdateConfig(com.example.heroku.model.DeviceConfig.builder().color("#333333").group_id(group).build())
                .block();
        this.deviceConfig.GetConfig(group)
                .as(StepVerifier::create)
                .consumeNextWith(config -> {
                            assertThat(config.getColor()).isEqualTo("#333333");
                        }
                )
                .verifyComplete();

        this.clientDevice.bootStrapData(group)
                .as(StepVerifier::create)
                .consumeNextWith(config -> {
                            assertThat(config.getDeviceConfig().getColor()).isEqualTo("#333333");
                        }
                )
                .verifyComplete();
    }

    public void DeviceConfigTest() {
        this.deviceConfig
                .UpdateConfig(com.example.heroku.model.DeviceConfig.builder().color("#ffffff").group_id(group).build())
                .block();
        this.deviceConfig.GetConfig(group)
                .as(StepVerifier::create)
                .consumeNextWith(config -> {
                            assertThat(config.getColor()).isEqualTo("#ffffff");
                        }
                )
                .verifyComplete();

        this.deviceConfig
                .UpdateConfig(com.example.heroku.model.DeviceConfig.builder().color("#333333").group_id(group).build())
                .block();
        this.deviceConfig.GetConfig(group)
                .as(StepVerifier::create)
                .consumeNextWith(config -> {
                            assertThat(config.getColor()).isEqualTo("#333333");
                        }
                )
                .verifyComplete();

        this.clientDevice.bootStrapData(group)
                .as(StepVerifier::create)
                .consumeNextWith(config -> {
                            assertThat(config.getDeviceConfig().getColor()).isEqualTo("#333333");
                            assertThat((long) config.getCarousel().size()).isEqualTo(4);
                        }
                )
                .verifyComplete();
    }
}
