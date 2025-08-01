package com.example.heroku.config.maper;

import com.example.heroku.model.productprice.ProductPriceItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.postgresql.codec.Json; // This is the correct type to import
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.nio.charset.StandardCharsets;

@WritingConverter
public class ProductPriceItemArrayToJsonConverter implements Converter<ProductPriceItem[], Json> {

    private final ObjectMapper objectMapper;

    public ProductPriceItemArrayToJsonConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Json convert(ProductPriceItem[] source) {
        if (source == null) {
            return null;
        }

        try {
            // Use ObjectMapper to write the ProductPriceItem[] array as a JSON string
            String jsonString = objectMapper.writeValueAsString(source);
            // Wrap the JSON string bytes into the R2DBC PostgreSQL Json type
            return Json.of(jsonString.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            // Handle serialization errors
            System.err.println("Error converting ProductPriceItem[] to JSON: " + e.getMessage());
            throw new RuntimeException("Failed to convert ProductPriceItem[] to JSON", e);
        }
    }
}