package com.example.heroku;

import com.example.heroku.converter.Base64byteConverter;
import com.example.heroku.model.repository.ImageRepository;
import lombok.Builder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class ImageTest {
    String imageContent;

    ImageRepository imageRepository;

    com.example.heroku.services.Image imageAPI;

    public void ImageTest() {
        FilePart filePart = new FilePart() {
            @Override
            public String filename() {
                return "test.png";
            }

            @Override
            public Mono<Void> transferTo(Path dest) {
                return Mono.empty();
            }

            @Override
            public String name() {
                return "test.png";
            }

            @Override
            public HttpHeaders headers() {
                return HttpHeaders.EMPTY;
            }

            @Override
            public Flux<DataBuffer> content() {
                return DataBufferUtils.read(
                        new ByteArrayResource(new Base64byteConverter().convertToEntityAttribute(imageContent)), new DefaultDataBufferFactory(), 1024);
            }
        };

        imageAPI.DeleteAll()
                .thenMany(Flux.just("1","2","3","4")
                        .flatMap(name->
                                imageAPI.Upload(Flux.just(filePart), "Carousel")
                        )
                )
                .then()
                .block();
        imageAPI.GetAll("Carousel")
                .map(image -> Integer.parseInt(image.getId()))
                .flatMap(id -> this.imageRepository.findById(id))
                .as(StepVerifier::create)
                .consumeNextWith(image -> {
                            assertThat(image.getContent()).isEqualTo(new Base64byteConverter().convertToEntityAttribute(imageContent));
                        }
                )
                .consumeNextWith(image -> {
                            assertThat(image.getContent()).isEqualTo(new Base64byteConverter().convertToEntityAttribute(imageContent));
                        }
                )
                .consumeNextWith(image -> {
                            assertThat(image.getContent()).isEqualTo(new Base64byteConverter().convertToEntityAttribute(imageContent));
                        }
                )
                .consumeNextWith(image -> {
                            assertThat(image.getContent()).isEqualTo(new Base64byteConverter().convertToEntityAttribute(imageContent));
                        }
                )
                .verifyComplete();
    }
}
