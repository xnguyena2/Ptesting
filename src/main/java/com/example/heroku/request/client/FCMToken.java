package com.example.heroku.request.client;

import com.example.heroku.model.UserFCM;
import com.example.heroku.request.client.base.BaseID;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FCMToken extends BaseID {
    private String device_id;

    private String fcm_id;

    public UserFCM covertModel() {
        return UserFCM.builder()
                .id(null)
                .device_id(this.device_id)
                .fcm_id(this.fcm_id)
                .group_id(this.getGroup_id())
                .build()
                .AutoFill();
    }
}
