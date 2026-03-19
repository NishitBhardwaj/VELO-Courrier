package com.velo.courrier.booking.pod;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class StorageService {

    @Value("${minio.bucket:pod-files}")
    private String minioBucket;

    @Value("${minio.url:http://localhost:9000}")
    private String minioUrl;

    @Value("${minio.access.key:minioadmin}")
    private String accessKey;

    @Value("${minio.secret.key:minioadmin}")
    private String secretKey;

    public String uploadFileAndGetSignedUrl(MultipartFile file, String prefix) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(minioUrl)
                    .credentials(accessKey, secretKey)
                    .build();

            String filename = prefix + "-" + UUID.randomUUID() + "-" + file.getOriginalFilename();
            log.info("Uploading file {} to bucket {}", filename, minioBucket);

            try (InputStream is = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(minioBucket)
                                .object(filename)
                                .stream(is, file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build()
                );
            }

            // Generate Presigned URL for 10 minutes
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(minioBucket)
                            .object(filename)
                            .expiry(10, TimeUnit.MINUTES)
                            .build()
            );

        } catch (Exception e) {
            log.error("Error occurred while generating presigned URL for MinIO", e);
            throw new RuntimeException("Error occurred while uploading POD file to object storage", e);
        }
    }
}
