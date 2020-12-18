package com.example.heroku.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Base64;

@Converter
public class Base64byteConverter implements AttributeConverter<byte[], String> {

    @Override
    public String convertToDatabaseColumn(byte[] content) {
        String encoded = Base64.getEncoder().encodeToString(content);
        return encoded;
    }

    @Override
    public byte[] convertToEntityAttribute(String content) {
        byte[] decodedBytes = Base64.getMimeDecoder().decode(content);
        return decodedBytes;
    }
}
