package org.quantum.minio.plus.service.impl;

import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.quantum.minio.plus.dto.ObjectDTO;
import org.quantum.minio.plus.dto.query.ObjectQuery;
import org.quantum.minio.plus.service.ObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author ike
 * @date 2021 年 03 月 29 日 18:09
 */
@Service
public class ObjectServiceImpl implements ObjectService {

    private MinioClient minioClient;

    @Autowired
    public void setMinioClient(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public List<ObjectDTO> getList(ObjectQuery query) {
        List<ObjectDTO> dtos = new ArrayList<>();
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(query.getBucketName()).build());
        results.forEach(result -> {
            ObjectDTO dto = new ObjectDTO();
            try {
                Item item = result.get();
                dto.setEtag(item.etag());
                dto.setObjectName(item.objectName());
                dto.setSize(item.size());

                if(!item.isDir()){
                    dto.setLastModified(item.lastModified());
                }

                dto.setStorageClass(item.storageClass());
                dto.setDir(item.isDir());
            } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
                // 有问题
            }
            dtos.add(dto);
        });
        dtos.sort((s1, s2) -> {
            if(s1.getDir() ^ s2.getDir()){
                return s1.getDir()? -1 : 1;
            }else{
                return 0;
            }
        });
        return dtos;
    }

    @Override
    public void create(ObjectDTO dto, InputStream inputStream) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(dto.getBucketName())
                            .object(dto.getObjectName())
                            .stream(inputStream, dto.getSize(), -1)
                            .contentType(dto.getContentType())
                            .build()
            );
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create(ObjectDTO dto) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(dto.getBucketName())
                            .object(dto.getObjectName())
                            .stream(new ByteArrayInputStream(new byte[] {}), 0, -1)
                            .build()
            );
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
