package com.example.heroku.services;

import com.example.heroku.model.repository.ImageRepository;
import com.example.heroku.photo.PhotoLib;
import com.example.heroku.request.carousel.IDContainer;
import com.example.heroku.response.Format;
import com.example.heroku.util.Util;
import org.apache.commons.io.IOUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Timestamp;
import java.util.Date;

import static org.springframework.http.ResponseEntity.ok;

@Component
public class Image {

    @Autowired
    ImageRepository imageRepository;

    public Mono<ResponseEntity<Format>> Upload(Flux<FilePart> file, String category) {
        return file.flatMap(f -> f.content().map(DataBuffer::asInputStream))
                .reduce(SequenceInputStream::new)
                .flatMap(initialStream -> {
                    String id = Util.getInstance().GenerateID();
                    PhotoLib.getInstance().uploadFile(initialStream, "/" + id);
                    return this.imageRepository.save(com.example.heroku.model.Image.builder()
                            .imgid(id)
                            .category(category)
                            .createat(new Timestamp(new Date().getTime()))
                            .build());
                })
                .map(save -> ok(Format.builder().response(save.getImgid()).build()));
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

    private InputStream createThumbnailIMG(InputStream is){
        try{
            BufferedImage img = toBufferedImage(is);
            return toByteArrayStream(Scalr.resize(img, 300));
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public byte[] CreateThumbnailIMG(byte[] content, int targetSize){
        try{
            return toByteArray(Scalr.resize(toBufferedImage(content), targetSize));
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    private BufferedImage toBufferedImage(InputStream is)
            throws IOException {
        return ImageIO.read(is);
    }

    private BufferedImage toBufferedImage(byte[] bytes)
            throws IOException {

        InputStream is = new ByteArrayInputStream(bytes);
        BufferedImage bi = ImageIO.read(is);
        return bi;

    }
    private InputStream toByteArrayStream(BufferedImage bi)
            throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, "png", baos);
        return new ByteArrayInputStream(baos.toByteArray());

    }

    private byte[] toByteArray(BufferedImage bi)
            throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, "png", baos);
        return baos.toByteArray();

    }
}
