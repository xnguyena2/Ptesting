package com.example.heroku.request.order;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CloseOrderRequest {

    private String group_id;

    private String id;

    private String status;
}
