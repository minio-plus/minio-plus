package org.quantum.minio.plus.service.impl;

import org.quantum.minio.plus.dto.MultipartUploadDTO;
import org.quantum.minio.plus.dto.UploadPartDTO;
import org.quantum.minio.plus.service.MultipartUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ike
 * @date 2021 年 05 月 09 日 17:33
 */
@Service
public class MultipartUploadServiceImpl implements MultipartUploadService {

    private S3Client s3Client;

    @Autowired
    public void setMinioClient(S3Client s3Client) {
        this.s3Client = s3Client;
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
    public MultipartUploadDTO createMultipartUpload(MultipartUploadDTO inputDto) {
        CreateMultipartUploadResponse response = s3Client.createMultipartUpload(CreateMultipartUploadRequest.builder()
                .bucket(inputDto.getBucketName())
                .key(inputDto.getKey())
                .metadata(inputDto.getMetadata())
                .contentType(inputDto.getContentType())
                .build());

        MultipartUploadDTO dto = new MultipartUploadDTO();
        dto.setUploadId(response.uploadId());
        dto.setKey(response.key());
        return dto;
    }

    @Override
    public String completeMultipartUpload(MultipartUploadDTO inputDto) {
        List<CompletedPart> completedParts = this.toCompletedPartList(inputDto.getParts());

        CompleteMultipartUploadRequest request = CompleteMultipartUploadRequest.builder()
                .bucket(inputDto.getBucketName())
                .uploadId(inputDto.getUploadId())
                .key(inputDto.getKey())
                .multipartUpload(CompletedMultipartUpload.builder()
                        .parts(completedParts)
                        .build())
                .build();
        CompleteMultipartUploadResponse response = s3Client.completeMultipartUpload(request);
        return response.location();
    }

    @Override
    public List<CompletedPart> toCompletedPartList(List<UploadPartDTO> dtos) {
        List<CompletedPart> completedParts = new ArrayList<>();
        dtos.forEach(part -> {
            completedParts.add(CompletedPart.builder()
                    .eTag(part.getETag())
                    .partNumber(part.getPartNumber())
                    .build());
        });
        return completedParts;
    }
}
