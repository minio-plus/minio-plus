package org.quantum.minio.plus.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author ike
 * @date 2021 年 05 月 06 日 11:23
 */
@Configuration
public class S3Config {

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Bean
    public S3Client s3Client() throws URISyntaxException {
        S3Client s3Client = S3Client.builder()
                .credentialsProvider(() ->  AwsBasicCredentials.create(accessKey, secretKey))
                .endpointOverride(new URI(this.endpoint))
                .region(Region.US_WEST_1)
                .build();
        return s3Client;
    }

    @Bean
    public S3Presigner s3Presigner() throws URISyntaxException {
        S3Presigner s3Presigner = S3Presigner.builder()
                .credentialsProvider(() -> AwsBasicCredentials.create(accessKey, secretKey))
                .endpointOverride(new URI(this.endpoint))
                .region(Region.US_WEST_1)
                .build();
        return s3Presigner;
    }
}
