package com.repsy.storage;

import io.minio.*;
import io.minio.errors.MinioException;

import java.io.InputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


public class MinioStorageService implements StorageService {

    private final MinioClient minioClient;
    private final String bucketName = "repsy-packages";

    public MinioStorageService() throws Exception {
        this.minioClient = MinioClient.builder()
                .endpoint("http://localhost:9000")
                .credentials("minioadmin", "minioadmin")
                .build();

        boolean exists = minioClient.bucketExists(
            BucketExistsArgs.builder().bucket(bucketName).build()
        );

        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    @Override
    public void save(String path, InputStream content) throws IOException {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(path)
                            .stream(content, content.available(), -1)
                            .build()
            );
        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new IOException("MinIO upload error: " + e.getMessage(), e);
        }
    }

    @Override
    public InputStream load(String path) throws IOException {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(path)
                            .build()
            );
        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new IOException("MinIO download error: " + e.getMessage(), e);
        }
    }
}
