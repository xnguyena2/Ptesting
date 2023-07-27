package com.example.heroku;

import com.example.heroku.model.repository.ImageRepository;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class ImageTest {

    private static byte[] getImageBytes(String imageUrl) {
        try {
            URL url = new URL(imageUrl);

            ByteArrayOutputStream output = new ByteArrayOutputStream();

            try (InputStream stream = url.openStream()) {
                byte[] buffer = new byte[4096];

                while (true) {
                    int bytesRead = stream.read(buffer);
                    if (bytesRead < 0) {
                        break;
                    }
                    output.write(buffer, 0, bytesRead);
                }
            }

            return output.toByteArray();
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    static class imgMap {
        public String id;
        public byte[] content;

        public imgMap(String id, byte[] content) {
            this.id = id;
            this.content = content;
        }
    }

    ImageRepository imageRepository;

    com.example.heroku.services.Image imageAPI;

    static class FilePartIPL implements FilePart {
        String filePath;
        byte[] imageContent = null;

        public FilePartIPL(String filePath) {
            this.filePath = filePath;
            try {
                this.imageContent = Files.readAllBytes(new File(filePath).toPath());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        @NonNull
        @Override
        public String filename() {
            return "test.png";
        }

        @NonNull
        @Override
        public Mono<Void> transferTo(@NonNull Path dest) {
            return Mono.empty();
        }

        @NonNull
        @Override
        public String name() {
            return "test.png";
        }

        @NonNull
        @Override
        public HttpHeaders headers() {
            return HttpHeaders.EMPTY;
        }

        @NonNull
        @Override
        public Flux<DataBuffer> content() {
            return DataBufferUtils.read(
                    new ByteArrayResource(imageContent), new DefaultDataBufferFactory(), 1024);
        }

        public byte[] getImageContent() {
            return imageContent;
        }
    }

    public void Run(String[] listImg) {
        Map<String, FilePartIPL> imgMap = new HashMap<>();
        FilePartIPL[] imgContent = new FilePartIPL[listImg.length];
        for (int i = 0; i < imgContent.length; i++) {
            imgContent[i] = new FilePartIPL(listImg[i]);
        }

        imageAPI.DeleteAll()
                .thenMany(Flux.just(imgContent)
                        .flatMap(img ->
                                imageAPI.Upload(Flux.just(img), "Carousel", Config.group)
                                        .map(formatResponseEntity -> {
                                            imgMap.put(Objects.requireNonNull(formatResponseEntity.getBody()).getImgid(), img);
                                            return formatResponseEntity;
                                        })
                        )
                )
                .then()
                .block();

        Flux.just(imgContent)
                .flatMap(img ->
                        imageAPI.Upload(Flux.just(img), "456", Config.group)
                                .map(formatResponseEntity -> {
                                    imgMap.put(Objects.requireNonNull(formatResponseEntity.getBody()).getImgid(), img);
                                    return formatResponseEntity;
                                })
                )
                .then()
                .block();

        imageAPI.GetAll(Config.group, "Carousel")
                .map(image -> new imgMap(image.getImgid(), getImageBytes(image.getLarge())))
                .as(StepVerifier::create)
                .consumeNextWith(image -> assertThat(image.content).isEqualTo(imgMap.get(image.id).getImageContent()))
                .consumeNextWith(image -> assertThat(image.content).isEqualTo(imgMap.get(image.id).getImageContent()))
                .consumeNextWith(image -> assertThat(image.content).isEqualTo(imgMap.get(image.id).getImageContent()))
                .consumeNextWith(image -> assertThat(image.content).isEqualTo(imgMap.get(image.id).getImageContent()))
                .verifyComplete();

    }
}
