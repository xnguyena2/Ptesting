package com.example.heroku.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

@Configuration
public class JacksonConfig {

    // Format: 2024-04-22T09:11:22.993 or with Z
    private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
            .optionalStart()
            .appendFraction(ChronoField.NANO_OF_SECOND, 1, 6, true) // old .appendFraction(ChronoField.NANO_OF_SECOND, 0, 6, true)
            .optionalEnd()
            .toFormatter();

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            JavaTimeModule module = new JavaTimeModule();

            // Deserializer that removes Z if present
            module.addDeserializer(LocalDateTime.class, new JsonDeserializer<>() {
                @Override
                public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                    String text = p.getText();
                    if (text == null || text.isBlank()) return null;

                    // Remove 'Z' if present
                    if (text.endsWith("Z")) {
                        text = text.substring(0, text.length() - 1);
                    }

                    return LocalDateTime.parse(text, FORMATTER);
                }
            });

            // Serializer without time zone
            module.addSerializer(LocalDateTime.class,
                    new LocalDateTimeSerializer(FORMATTER));

            builder.modules(module);
            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            builder.serializationInclusion(JsonInclude.Include.NON_NULL);
        };
    }
}
