package com.example.heroku;

import com.example.heroku.model.repository.ImageRepository;
import com.example.heroku.photo.PhotoLib;
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

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class ImageTest {

    class imgMap{
        public String id;
        public byte[] content;

        public imgMap(String id, byte[] content){
            this.id = id;
            this.content = content;
        }
    }

    ImageRepository imageRepository;

    com.example.heroku.services.Image imageAPI;

    class FilePartIPL implements FilePart {
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
                    new ByteArrayResource(imageContent), new DefaultDataBufferFactory(), 1024);
        }

        public byte[] getImageContent() {
            return imageContent;
        }
    }

    public void ImageTest(String[] listImg) {
        Map<String, FilePartIPL> imgMap = new HashMap();
        FilePartIPL[] imgContent = new FilePartIPL[listImg.length];
        for (int i = 0; i < imgContent.length; i++) {
            imgContent[i] = new FilePartIPL(listImg[i]);
        }

        imageAPI.DeleteAll()
                .thenMany(Flux.just(imgContent)
                        .flatMap(img ->
                                imageAPI.Upload(Flux.just(img), "Carousel")
                                .map(formatResponseEntity -> {imgMap.put(Objects.requireNonNull(formatResponseEntity.getBody()).getResponse(),img); return formatResponseEntity;})
                        )
                )
                .then()
                .block();

        Flux.just(imgContent)
                .flatMap(img ->
                        imageAPI.Upload(Flux.just(img), "456")
                                .map(formatResponseEntity -> {imgMap.put(Objects.requireNonNull(formatResponseEntity.getBody()).getResponse(),img); return formatResponseEntity;})
                )
                .then()
                .block();

        imageAPI.GetAll("Carousel")
                .map(image -> new imgMap(image.getImgid(), PhotoLib.getInstance().downloadFile(image.getImgid())))
                .as(StepVerifier::create)
                .consumeNextWith(image -> {
                    assertThat(image.content).isEqualTo(imgMap.get(image.id).getImageContent());
                })
                .consumeNextWith(image -> {
                    assertThat(image.content).isEqualTo(imgMap.get(image.id).getImageContent());
                })
                .consumeNextWith(image -> {
                    assertThat(image.content).isEqualTo(imgMap.get(image.id).getImageContent());
                })
                .consumeNextWith(image -> {
                    assertThat(image.content).isEqualTo(imgMap.get(image.id).getImageContent());
                })
                .verifyComplete();

        imageAPI.GetAll("Carousel")
                .map(image -> new imgMap(image.getImgid(), imageAPI.CreateThumbnailIMG(PhotoLib.getInstance().downloadFile(image.getImgid()), 500)))
                .as(StepVerifier::create)
                .consumeNextWith(image -> {
                    assertThat(image.content).isEqualTo(imageAPI.CreateThumbnailIMG(imgMap.get(image.id).getImageContent(), 500));
                })
                .consumeNextWith(image -> {
                    assertThat(image.content).isEqualTo(imageAPI.CreateThumbnailIMG(imgMap.get(image.id).getImageContent(), 500));
                })
                .consumeNextWith(image -> {
                    assertThat(image.content).isEqualTo(imageAPI.CreateThumbnailIMG(imgMap.get(image.id).getImageContent(), 500));
                })
                .consumeNextWith(image -> {
                    assertThat(image.content).isEqualTo(imageAPI.CreateThumbnailIMG(imgMap.get(image.id).getImageContent(), 500));
                })
                .verifyComplete();
    }
}
