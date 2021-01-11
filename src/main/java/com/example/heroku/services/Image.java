package com.example.heroku.services;

import com.example.heroku.model.repository.ImageRepository;
import com.example.heroku.request.carousel.IDContainer;
import com.example.heroku.response.Format;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.SequenceInputStream;
import java.sql.Timestamp;
import java.util.Date;

import static org.springframework.http.ResponseEntity.ok;

@Component
public class Image {

    @Autowired
    ImageRepository imageRepository;

    public Mono<Object> Upload(Flux<FilePart> file, String category){
        return file.flatMap(f -> f.content().map(it -> it.asInputStream()))
                .reduce(SequenceInputStream::new)
                .flatMap(initialStream -> {
                    try {
                        byte[] content = IOUtils.toByteArray(initialStream);
                        return this.imageRepository.save(com.example.heroku.model.Image.builder()
                                .content(content)
                                .category(category)
                                .createat(new Timestamp(new Date().getTime()))
                                .build());
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .map(save -> ok(Format.builder().response(save.getId()).build()));
    }

    public Mono<Object> Delete(IDContainer img){
        return Mono.just(img).flatMap(info->this.imageRepository.deleteById(Integer.parseInt(info.getId()))
                .map(deleted -> ok(Format.builder().response(deleted.getId()).build())));
    }

    public Flux<com.example.heroku.model.Image> GetAll(String category){
        return this.imageRepository.findByCategory(category);
    }

    public Mono<Void> DeleteAll(){
        return this.imageRepository.deleteAll();
    }
}
