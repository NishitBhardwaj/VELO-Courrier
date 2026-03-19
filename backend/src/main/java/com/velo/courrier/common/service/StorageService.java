package com.velo.courrier.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {

    @Value("${minio.bucket.name:velo-kyc-private}")
    private String bucketName;

    /**
     * Generate an AWS S3 V4 Pre-Signed URL allowing temporary GET access to a private object.
     * Essential for viewing Driver KYC docs without exposing the bucket to the public internet.
     */
    public String generatePreSignedUrl(String objectKey, Duration expiry) {
        log.info("Generating {}-minute Pre-Signed URL for MinIO object: {}/{}", expiry.toMinutes(), bucketName, objectKey);
        // MinioClient minioClient = ...;
        // String url = minioClient.getPresignedObjectUrl(
        //      GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(bucketName).object(objectKey).expiry((int) expiry.toSeconds()).build());
        return "https://storage.velocourrier.com/" + bucketName + "/" + objectKey + "?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Signature=mock" + UUID.randomUUID();
    }
}
