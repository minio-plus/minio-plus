package org.quantum.minio.plus.service.impl;

import org.quantum.minio.plus.dto.ComposeUploadPartDTO;
import org.quantum.minio.plus.dto.MultipartUploadDTO;
import org.quantum.minio.plus.dto.ObjectDTO;
import org.quantum.minio.plus.dto.UploadPartDTO;
import org.quantum.minio.plus.dto.query.ObjectQuery;
import org.quantum.minio.plus.dto.query.PartQuery;
import org.quantum.minio.plus.service.ObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.*;

import java.time.Duration;
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

    private S3Client s3Client;

    @Autowired
    public void setMinioClient(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public List<ObjectDTO> getList(ObjectQuery query) {
        List<ObjectDTO> dtos = new ArrayList<>();

        ListObjectsRequest.Builder request = ListObjectsRequest.builder();
        request.bucket(query.getBucketName());

        List<String> prefixs = query.getPrefixs();
        if(Objects.nonNull(prefixs) && prefixs.size() > 0) {
            request.prefix(prefixs.stream().collect(Collectors.joining()));
        }

        ListObjectsResponse response = s3Client.listObjects(request.build());
        List<S3Object> s3Objects = response.contents();

        s3Objects.forEach(s3Object -> {
            ObjectDTO dto = new ObjectDTO();
            dto.setEtag(s3Object.eTag());
            dto.setObjectName(s3Object.key());
            dto.setSize(s3Object.size());
            dto.setStorageClass(s3Object.storageClassAsString());
            dto.setLastModified(s3Object.lastModified());

            if(s3Object.key().lastIndexOf("/") > 0) {
                dto.setDir(true);
            } else {
                dto.setDir(false);
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
    public void create(ObjectDTO dto) {
        String objectName = objectNameHandler(dto);
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(dto.getBucketName())
                .key(objectName)
                .metadata(dto.getMetadata())
                .build();
        s3Client.putObject(request, RequestBody.empty());
    }

    @Override
    public void delete(String bucketName, String objectName) {
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(objectName)
                .build();
        s3Client.deleteObject(request);
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
