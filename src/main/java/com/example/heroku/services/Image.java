package com.example.heroku.services;

import com.example.heroku.model.repository.ImageRepository;
import com.example.heroku.photo.FlickrLib;
import com.example.heroku.request.carousel.IDContainer;
import com.example.heroku.response.Format;
import com.flickr4java.flickr.FlickrException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.SequenceInputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.http.ResponseEntity.ok;

@Component
public class Image {

    @Autowired
    ImageRepository imageRepository;

    public Mono<ResponseEntity<com.example.heroku.model.Image>> Upload(Flux<FilePart> file, String category) {
        AtomicReference<String> fileName = new AtomicReference<>();
        return file.flatMap(f -> {
            fileName.set(f.filename());
            return f.content().map(DataBuffer::asInputStream);
        })
                .reduce(SequenceInputStream::new)
                .flatMap(initialStream -> {
                    String[] info = null;//Util.getInstance().GenerateID();
                    try {
                        info = FlickrLib.getInstance().uploadfile(initialStream, fileName.get());
                    } catch (FlickrException e) {
                        e.printStackTrace();
                        return Mono.just(com.example.heroku.model.Image.builder().build());
                    }
                    return this.imageRepository.save(com.example.heroku.model.Image.builder()
                            .imgid(info[0])
                            .thumbnail(info[1])
                            .medium(info[2])
                            .large(info[3])
                            .category(category)
                            .createat(new Timestamp(new Date().getTime()))
                            .build());
                })
                .map(ResponseEntity::ok);
    }

    public Mono<Object> Delete(IDContainer img) {
        return Mono.just(img).flatMap(info ->
                {
                    try {
                        FlickrLib.getInstance().DeleteImage(info.getId());
                    } catch (FlickrException e) {
                        e.printStackTrace();
                    }
                    return this.imageRepository.deleteImage(info.getId())
                            .map(deleted ->
                                    ok(Format.builder().response(deleted.getId()).build())
                            );
                }
        );
    }

    public Flux<com.example.heroku.model.Image> GetAll(String category){
        return this.imageRepository.findByCategory(category);
    }

    public Mono<Void> DeleteAll(){
        return this.imageRepository.deleteAll();
    }
}
