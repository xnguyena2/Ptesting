package com.example.heroku.request.client;

import com.example.heroku.model.UserFCM;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FCMToken {
    private String group_id;
    private String device_id;

    private String fcm_id;

    public UserFCM covertModel() {
        return UserFCM.builder()
                .id(null)
                .device_id(this.device_id)
                .fcm_id(this.fcm_id)
                .group_id(this.group_id)
                .build()
                .AutoFill();
    }
}
