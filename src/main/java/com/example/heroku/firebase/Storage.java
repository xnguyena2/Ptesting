package com.example.heroku.firebase;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
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

    private static volatile Bucket bucket;

    private Bucket getBucket() {
        if (!myFireBase.isAuthSuccess()) return null;

        if (bucket == null) {
            synchronized (Storage.class) {
                if (bucket == null) {
                    bucket = StorageClient.getInstance().bucket(properties.getBucketName());
                    log.info("Firebase bucket initialized: {}", properties.getBucketName());
                }
            }
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
        String encoded = URLEncoder.encode(name, StandardCharsets.UTF_8);
        return String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media", properties.getBucketName(), encoded);
    }

    @Override
    public String save(MultipartFile file) throws IOException {

        Bucket bucket = getBucket();

        if (bucket == null) {
            throw new IOException("Firebase bucket is not initialized or authentication failed.");
        }

        String name = "img/" + generateFileName(file.getOriginalFilename());

        bucket.create(name, file.getBytes(), file.getContentType());

        log.info("Saved file: {}", name);

        return name;
    }

    @Override
    public String save(BufferedImage bufferedImage, String originalFileName) throws IOException {

        Bucket bucket = getBucket();

        if (bucket == null) {
            throw new IOException("Firebase bucket is not initialized or authentication failed.");
        }

        byte[] bytes = getByteArrays(bufferedImage, getExtension(originalFileName));

        String name = "img/" + generateFileName(originalFileName);

        bucket.create(name, bytes);

        log.info("Saved image from BufferedImage: {}", name);

        return name;
    }

    @Override
    public String[] save(InputStream inputStream, String originalFileName) throws IOException {

        Bucket bucket = getBucket();

        String suffix = originalFileName.substring(originalFileName.lastIndexOf('.') + 1);

        String contentType = switch (suffix.toLowerCase()) {
            case "png" -> "image/png";
            case "mpg", "mpeg" -> "video/mpeg";
            case "mov" -> "video/quicktime";
            default -> "image/jpeg";
        };

        String name = "img/" + generateFileName(originalFileName);

        bucket.create(name, inputStream, contentType);

        log.info("Uploaded file: {} as {}", originalFileName, name);

        String url = getImageUrl(name);
        return new String[]{name, url, url, url};
    }

    @Override
    public void delete(String name) throws IOException {

        if (!StringUtils.hasLength(name)) {
            throw new IOException("invalid file name");
        }

        com.google.cloud.storage.Storage storage = myFireBase.getStorage();
        if (storage != null) {
            // Define the blob ID
            BlobId blobId = BlobId.of(properties.getBucketName(), name);

            // Delete the blob
            boolean deleted = storage.delete(blobId);

            if (deleted) {
                log.info("File {} deleted from bucket {}", name, properties.getBucketName());
            } else {
                log.warn("File {} not found in bucket {}", name, properties.getBucketName());
            }
        } else {

            Bucket bucket = getBucket();
            if (bucket == null) {
                throw new NullPointerException("bucket is null");
            }

            Blob blob = bucket.get(name);

            if (blob == null) {
                throw new IOException("file not found");
            }

            blob.delete();
            log.info("Deleted file: {}", name);
        }
    }

    @Override
    public void deleteAll() throws IOException {
        Bucket bucket = getBucket();
        if (bucket == null) throw new IOException("Firebase bucket not available");

        int deletedCount = 0;
        String prefix = "img/"; // only delete files under this prefix

        log.info("Deleting all files with prefix: {}", prefix);

        for (Blob blob : bucket.list(com.google.cloud.storage.Storage.BlobListOption.prefix(prefix)).iterateAll()) {
            boolean deleted = blob.delete();
            if (deleted) {
                deletedCount++;
                log.info("Deleted: {}", blob.getName());
            } else {
                log.warn("Failed to delete: {}", blob.getName());
            }
        }

        log.info("Total files deleted: {}", deletedCount);
    }

}
