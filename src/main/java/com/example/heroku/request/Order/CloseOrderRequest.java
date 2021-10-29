package com.example.heroku.request.Order;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CloseOrderRequest {
    private String id;

    private String status;
}
