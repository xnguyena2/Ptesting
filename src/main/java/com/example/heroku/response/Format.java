package com.example.heroku.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Format {
    private String response;

    public void setResponse(int response) {
        this.response = String.valueOf(response);
    }
    public Format setResponse(Long response) {
        this.response = String.valueOf(response);
        return this;
    }
}
