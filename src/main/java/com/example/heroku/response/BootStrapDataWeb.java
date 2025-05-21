package com.example.heroku.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder()
public class BootStrapDataWeb extends BootStrapData {

    private String web_config;

    public BootStrapDataWeb setWeb_config(String web_config) {
        this.web_config = web_config;
        return this;
    }
}
