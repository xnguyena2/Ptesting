package com.example.heroku.request.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackageID {
    private String group_id;
    private String device_id;
    private String package_id;
    private int page;
    private int size;
}