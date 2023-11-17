package com.example.dynamodb.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.net.URL;
import java.time.Duration;

@Service
public class GetS3LinkPresigned {

    private final S3Presigner s3Presigner;

    public GetS3LinkPresigned(S3Presigner s3Presigner) {
        this.s3Presigner = s3Presigner;
    }

    public URL getUri() {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket("bucket")
                .key("arquivo.png")
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(1))
                .getObjectRequest(getObjectRequest)
                .build();

        return s3Presigner.presignGetObject(presignRequest).url();
    }
}
