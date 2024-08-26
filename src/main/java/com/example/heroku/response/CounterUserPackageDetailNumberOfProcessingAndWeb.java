package com.example.heroku.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CounterUserPackageDetailNumberOfProcessingAndWeb {

    private int processing;
    private int web;
}
