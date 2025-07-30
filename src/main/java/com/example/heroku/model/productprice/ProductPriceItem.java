package com.example.heroku.model.productprice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data // Generates getters, setters, equals, hashCode, toString
@NoArgsConstructor // Generates no-arg constructor (ESSENTIAL for Jackson)
@AllArgsConstructor // Generates constructor with all fields
@Builder // Generates a builder pattern (optional, but good practice)
public class ProductPriceItem {

    private float price;
    private int amount;
    private LocalDateTime createat;
}
