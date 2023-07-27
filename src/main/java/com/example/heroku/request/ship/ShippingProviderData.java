package com.example.heroku.request.ship;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingProviderData {

    private String id;
    private String group_id;
    private String json;

}
