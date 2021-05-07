package org.quantum.minio.plus.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    public AmazonS3 s3Client(){
        AmazonS3 amazonS3 = new AmazonS3Client(new AWSCredentialsProvider() {
            @Override
            public AWSCredentials getCredentials() {
                return new BasicAWSCredentials(accessKey, secretKey);
            }

            @Override
            public void refresh() {

            }
        });
        amazonS3.setEndpoint(this.endpoint);
        return amazonS3;
    }
}
