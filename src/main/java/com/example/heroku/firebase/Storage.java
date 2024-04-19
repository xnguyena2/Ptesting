package com.example.heroku.firebase;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
@RequiredArgsConstructor
public class Storage implements IImageService {

    @Autowired
    Properties properties;

    @Autowired
    MyFireBase myFireBase;

    private static Bucket bucket;

    Bucket getBucket() {
        if (!myFireBase.isAuthSuccess()) {
            return null;
        }
        if (bucket == null) {
            bucket = StorageClient.getInstance().bucket(properties.getBucketName());
        }
        return bucket;
    }

    @Data
    @Configuration
    @ConfigurationProperties(prefix = "firebase")
    public static class Properties {

        private String bucketName;
    }


    @Override
    public boolean isEnable() {
        return myFireBase.isAuthSuccess();
    }

    @Override
    public String getImageUrl(String name) {
        String encoded = name;
        try {
            encoded = URLEncoder.encode(name, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media", properties.getBucketName(), encoded);
    }

    @Override
    public String save(MultipartFile file) throws IOException {

        Bucket bucket = getBucket();

        String name = "img/" + generateFileName(file.getOriginalFilename());

        bucket.create(name, file.getBytes(), file.getContentType());

        return name;
    }

    @Override
    public String save(BufferedImage bufferedImage, String originalFileName) throws IOException {

        byte[] bytes = getByteArrays(bufferedImage, getExtension(originalFileName));

        Bucket bucket = getBucket();

        String name = "img/" + generateFileName(originalFileName);

        bucket.create(name, bytes);

        return name;
    }

    @Override
    public String[] save(InputStream inputStream, String originalFileName) throws IOException {

        Bucket bucket = getBucket();

        String suffix = originalFileName.substring(originalFileName.lastIndexOf('.') + 1);
        String contentType = "image/jpeg";

        if (suffix.equalsIgnoreCase("png")) {
            contentType = "image/png";
        } else if (suffix.equalsIgnoreCase("mpg") || suffix.equalsIgnoreCase("mpeg")) {
            contentType = "video/mpeg";
        } else if (suffix.equalsIgnoreCase("mov")) {
            contentType = "video/quicktime";
        }

        String name = "img/" + generateFileName(originalFileName);

        System.out.println(" File : " + originalFileName);
        System.out.println(" basefilename : " + name);


        bucket.create(name, inputStream, contentType);

        System.out.println(" File : " + originalFileName + " uploaded: photoId = " + name);

        String[] allImg = new String[4];
        String url = getImageUrl(name);
        allImg[0] = name;
        allImg[1] = url;
        allImg[2] = url;
        allImg[3] = url;
        return allImg;
    }

    @Override
    public void delete(String name) throws IOException {

        Bucket bucket = getBucket();

        if (!StringUtils.hasLength(name)) {
            throw new IOException("invalid file name");
        }

        Blob blob = bucket.get(name);

        if (blob == null) {
            throw new IOException("file not found");
        }

        blob.delete();
    }
}
