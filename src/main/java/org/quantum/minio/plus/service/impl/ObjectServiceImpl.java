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

    private S3Presigner s3Presigner;

    @Autowired
    public void setMinioClient(S3Client s3Client, S3Presigner s3Presigner) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
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
    public MultipartUploadDTO initiateMultipartUpload(MultipartUploadDTO inputDto) {
        CreateMultipartUploadResponse response = s3Client.createMultipartUpload(CreateMultipartUploadRequest.builder()
                .bucket(inputDto.getBucketName())
                .key(inputDto.getKey())
                .metadata(inputDto.getMetadata())
                .build());

        MultipartUploadDTO dto = new MultipartUploadDTO();
        dto.setUploadId(response.uploadId());
        dto.setKey(response.key());
        return dto;
    }

    @Override
    public List<MultipartUploadDTO> getMultipartUploadList(String bucketName) {
        List<MultipartUploadDTO> dtos = new ArrayList<>();

        ListMultipartUploadsResponse response = s3Client.listMultipartUploads(ListMultipartUploadsRequest.builder()
                .bucket(bucketName)
                .build());

        List<MultipartUpload> multipartUploads = response.uploads();
        multipartUploads.forEach(multipartUpload -> {
            MultipartUploadDTO dto = new MultipartUploadDTO();
            dto.setKey(multipartUpload.key());
            dto.setUploadId(multipartUpload.uploadId());
            dto.setStorageClass(multipartUpload.storageClassAsString());
            dto.setInitiated(multipartUpload.initiated());
            dtos.add(dto);
        });
        return dtos;
    }

    @Override
    public List<UploadPartDTO> getUploadPartList(PartQuery partQuery) {
        List<UploadPartDTO> dtos = new ArrayList<>();

        ListPartsResponse response = s3Client.listParts(ListPartsRequest.builder()
                .bucket(partQuery.getBucketName())
                .key(partQuery.getKey())
                .uploadId(partQuery.getUploadId())
                .build());

        List<Part> partSummaries = response.parts();
        partSummaries.forEach(part -> {
            UploadPartDTO dto = new UploadPartDTO();
            dto.setETag(part.eTag());
            dto.setSize(part.size());
            dto.setPartNumber(part.partNumber());
            dtos.add(dto);
        });
        return dtos;
    }

    @Override
    public String composeUploadPart(ComposeUploadPartDTO dto) {
        s3Client.completeMultipartUpload(CompleteMultipartUploadRequest.builder()
                .bucket(dto.getBucketName())
                .key(dto.getKey())
                .uploadId(dto.getUploadId())
                .build());
        return this.getPresignedUrl(dto.getBucketName(), dto.getKey());
    }

    @Override
    public String getPresignedUrl(String bucketName, String key) {
        GetObjectRequest getObjectRequest =
                GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build();

        GetObjectPresignRequest getObjectPresignRequest =
                GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(10))
                        .getObjectRequest(getObjectRequest)
                        .build();

        PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(getObjectPresignRequest);
        return presignedGetObjectRequest.url().toString();
    }

    @Override
    public String getPresignedUrl(ObjectDTO dto) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(dto.getObjectName())
                .key(dto.getObjectName())
                .contentType("text/plain")
                .metadata(dto.getMetadata())
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(objectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
        return presignedRequest.url().toString();
    }

    @Override
    public String getPresignUploadPartUrl(UploadPartDTO dto) {
        UploadPartPresignRequest request = UploadPartPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .uploadPartRequest(UploadPartRequest.builder()
                        .bucket(dto.getBucketName())
                        .key(dto.getKey())
                        .partNumber(dto.getPartNumber())
                        .uploadId(dto.getUploadId())
                        .build())
                .build();
        PresignedUploadPartRequest presignedRequest = s3Presigner.presignUploadPart(request);
        return presignedRequest.url().toString();
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
