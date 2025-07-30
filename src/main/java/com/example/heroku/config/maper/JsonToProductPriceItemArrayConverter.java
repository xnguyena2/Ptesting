package com.example.heroku.config.maper;

import com.example.heroku.model.productprice.ProductPriceItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.postgresql.codec.Json; // This is the correct type to import
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class JsonToProductPriceItemArrayConverter implements Converter<Json, ProductPriceItem[]> {

    private final ObjectMapper objectMapper;

    public JsonToProductPriceItemArrayConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ProductPriceItem[] convert(Json source) {
        if (source == null) {
            return null;
        }
        byte[] bytes = source.asArray();
        try {
//            return objectMapper.readValue(bytes, new TypeReference<ProductPriceItem[]>() {});
            return objectMapper.readValue(bytes, ProductPriceItem[].class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert JSON to ProductPriceItem[]", e);
        }
    }
}