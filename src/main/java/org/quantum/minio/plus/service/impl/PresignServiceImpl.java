package org.quantum.minio.plus.service.impl;

import org.quantum.minio.plus.dto.MultipartUploadDTO;
import org.quantum.minio.plus.dto.ObjectDTO;
import org.quantum.minio.plus.dto.PutObjectDTO;
import org.quantum.minio.plus.dto.UploadPartDTO;
import org.quantum.minio.plus.dto.query.ObjectQuery;
import org.quantum.minio.plus.service.MultipartUploadService;
import org.quantum.minio.plus.service.PresignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ike
 * @date 2021 年 05 月 09 日 17:11
 */
@Service
public class PresignServiceImpl implements PresignService {

    private S3Presigner s3Presigner;

    private MultipartUploadService multipartUploadService;

    @Autowired
    public void setMinioClient(S3Presigner s3Presigner, MultipartUploadService multipartUploadService) {
        this.s3Presigner = s3Presigner;
        this.multipartUploadService = multipartUploadService;
    }

    @Override
    public String getObject(ObjectQuery query) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                        .bucket(query.getBucketName())
                        .key(query.getKey())
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
    public String putObject(PutObjectDTO dto) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(dto.getBucketName())
                .key(dto.getObjectName())
                .contentType(dto.getContentType())
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
    public String uploadPart(UploadPartDTO dto) {
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
    public String abortMultipartUpload(MultipartUploadDTO dto) {
        AbortMultipartUploadPresignRequest abortMultipartUploadPresignRequest = AbortMultipartUploadPresignRequest.builder()
                .abortMultipartUploadRequest(AbortMultipartUploadRequest.builder()
                        .bucket(dto.getBucketName())
                        .key(dto.getKey())
                        .uploadId(dto.getUploadId())
                        .build())
                .signatureDuration(Duration.ofMinutes(5))
                .build();
        PresignedAbortMultipartUploadRequest request = s3Presigner.presignAbortMultipartUpload(abortMultipartUploadPresignRequest);
        return request.url().toString();
    }

    @Override
    public String completeMultipartUpload(MultipartUploadDTO dto) {
        List<CompletedPart> completedParts = multipartUploadService.toCompletedPartList(dto.getParts());

        CompleteMultipartUploadPresignRequest completeRequest = CompleteMultipartUploadPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .completeMultipartUploadRequest(CompleteMultipartUploadRequest.builder()
                        .bucket(dto.getBucketName())
                        .uploadId(dto.getUploadId())
                        .key(dto.getKey())
                        .multipartUpload(CompletedMultipartUpload.builder()
                                .parts(completedParts)
                                .build())
                        .build())
                .build();
        PresignedCompleteMultipartUploadRequest request = s3Presigner.presignCompleteMultipartUpload(completeRequest);
        return request.url().toString();
    }
}
