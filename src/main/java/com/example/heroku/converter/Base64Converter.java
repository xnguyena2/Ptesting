package com.example.heroku.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Base64;

@Converter
public class Base64Converter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String raw) {
        String encoded = Base64.getEncoder().encodeToString(raw.getBytes());
        return encoded;
    }

    @Override
    public String convertToEntityAttribute(String encoded) {
        byte[] decodedBytes = Base64.getMimeDecoder().decode(encoded);
        return new String(decodedBytes);
    }
}
