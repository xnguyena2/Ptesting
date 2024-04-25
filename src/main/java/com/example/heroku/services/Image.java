package com.example.heroku.services;

import com.example.heroku.firebase.IImageService;
import com.example.heroku.firebase.Storage;
import com.example.heroku.model.Product;
import com.example.heroku.model.repository.ImageRepository;
import com.example.heroku.photo.FlickrLib;
import com.example.heroku.request.carousel.IDContainer;
import com.example.heroku.response.Format;
import com.example.heroku.util.Util;
import com.flickr4java.flickr.FlickrException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.SequenceInputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.http.ResponseEntity.ok;

@Component
public class Image {

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    private FlickrLib flickrLib;

    @Autowired
    private Storage fireBaseStorage;


    private IImageService getStoreServices(boolean mustFlick) {
        if (mustFlick) {
            return flickrLib;
        }
        if (fireBaseStorage.isEnable()) {
            return fireBaseStorage;
        }
        return flickrLib;
    }


    public Mono<ResponseEntity<com.example.heroku.model.Image>> Upload(Flux<FilePart> file, String category, String groupID, String tag) {
        AtomicReference<String> fileName = new AtomicReference<>();
        return file.flatMap(f -> {
                    fileName.set(f.filename());
                    return f.content().map(DataBuffer::asInputStream);
                })
                .reduce(SequenceInputStream::new)
                .flatMap(initialStream -> {
                    String[] info = null;//Util.getInstance().GenerateID();
                    try {
                        info = getStoreServices(false).save(initialStream, fileName.get());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return this.imageRepository.save(com.example.heroku.model.Image.builder()
                            .imgid(info[0])
                            .thumbnail(info[1])
                            .medium(info[2])
                            .large(info[3])
                            .category(category)
                            .tag(tag)
                            .createat(Util.getInstance().Now())
                            .group_id(groupID)
                            .build());
                })
                .map(ResponseEntity::ok);
    }

    public Mono<ResponseEntity<Format>> Delete(IDContainer img) {
        return Mono.just(img).flatMap(info ->
                {
                    try {
                        String id = info.getId();
                        getStoreServices(!id.startsWith("img")).delete(id);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return this.imageRepository.deleteImage(img.getGroup_id(), info.getId())
                            .map(deleted ->
                                    ok(Format.builder().build().setResponse(deleted.getId()))
                            );
                }
        );
    }

    public Flux<com.example.heroku.model.Image> GetAll(String groupID, String category) {
        return this.imageRepository.findByCategory(groupID, category);
    }

    public Mono<Void> DeleteAll() {
        return this.imageRepository.findAll()
                .map(image -> {
                    try {
                        getStoreServices(false).delete(image.getImgid());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return image;
                })
                .then(this.imageRepository.deleteAll()
//                        .onErrorContinue((throwable, o) -> {
//                            System.out.println("Error while processing {" + o + "}. Cause: {" + throwable.getMessage() + "}");
//                        })
                );
    }

    public Flux<Boolean> JustDeleteImageFromFlickByGroupID(String groupID) {
        return imageRepository.findByGroupID(groupID)
                .map(image -> {
                    try {
                        getStoreServices(false).delete(image.getImgid());
                        return true;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                });
    }
}
