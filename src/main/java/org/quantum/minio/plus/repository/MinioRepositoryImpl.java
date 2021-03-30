package org.quantum.minio.plus.repository;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import io.minio.messages.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

/**
 * @author ike
 * @date 2021 年 03 月 29 日 17:08
 */
@Repository
public class MinioRepositoryImpl implements MinioRepository {

    private MinioClient minioClient;

    @Autowired
    public void setMinioClient(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public List<Bucket> getBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<Tags> getBucketTags(String name) {
        Tags tags = null;
        try {
            tags = minioClient.getBucketTags(GetBucketTagsArgs.builder().bucket(name).build());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return Optional.of(tags);
    }

    @Override
    public void createBucket(String name) {
        try {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(name)
                            .build()
            );
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return;
    }

    @Override
    public void deleteBucket(String name) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(name).build());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Iterable<Result<Item>> getObjects(ListObjectsArgs args) {
        Iterable<Result<Item>> results = minioClient.listObjects(args);
        return results;
    }


}
