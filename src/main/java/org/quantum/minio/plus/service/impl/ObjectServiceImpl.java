package org.quantum.minio.plus.service.impl;

import io.minio.*;
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
import java.util.stream.Collectors;

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

        ListObjectsArgs.Builder argsBuilder = ListObjectsArgs.builder();
        argsBuilder.bucket(query.getBucketName());
        List<String> prefixs = query.getPrefixs();
        if(Objects.nonNull(prefixs) && prefixs.size() > 0){
            argsBuilder.prefix(prefixs.stream().collect(Collectors.joining()));
        }

        Iterable<Result<Item>> results = minioClient.listObjects(argsBuilder.build());
        results.forEach(result -> {
            ObjectDTO dto = new ObjectDTO();
            try {
                Item item = result.get();
                dto.setEtag(item.etag());
                dto.setObjectName(item.objectName());
                dto.setSize(item.size());
                dto.setUserMetaData(item.userMetadata());

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
    public List getFragmentList() {
        return null;
    }

    @Override
    public void create(ObjectDTO dto, InputStream inputStream) {
        try {
            String objectName = this.objectNameHandler(dto);
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(dto.getBucketName())
                            .object(objectName)
                            .stream(inputStream, dto.getSize(), -1)
                            .contentType(dto.getContentType())
                            .userMetadata(dto.getUserMetaData())
                            .build()
            );
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create(ObjectDTO dto) {
        try {
            String objectName = objectNameHandler(dto);
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(dto.getBucketName())
                    .object(objectName)
                    .stream(new ByteArrayInputStream(new byte[] {}), 0, -1)
                    .build());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String bucketName, String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String objectNameHandler(ObjectDTO dto) {
        String targetName;
        List<String> prefixs = dto.getPrefixs();
        if(Objects.nonNull(prefixs) && prefixs.size() > 0){
            targetName = prefixs.stream().collect(Collectors.joining()) + dto.getObjectName();
        } else {
            targetName = dto.getObjectName();
        }
        return targetName;
    }
}
