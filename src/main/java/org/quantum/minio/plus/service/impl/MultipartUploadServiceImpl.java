package org.quantum.minio.plus.service.impl;

import org.quantum.minio.plus.ListResponse;
import org.quantum.minio.plus.ValueResponse;
import org.quantum.minio.plus.dto.MultipartUploadDTO;
import org.quantum.minio.plus.dto.UploadPartDTO;
import org.quantum.minio.plus.manager.MultipartUploadManager;
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

    private MultipartUploadManager multipartUploadManager;

    @Autowired
    public void setMinioClient(S3Client s3Client, MultipartUploadManager multipartUploadManager) {
        this.s3Client = s3Client;
        this.multipartUploadManager = multipartUploadManager;
    }

    @Override
    public ListResponse<MultipartUploadDTO> getMultipartUploadList(String bucketName) {
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
        return ListResponse.of(dtos);
    }

    @Override
    public ValueResponse<MultipartUploadDTO> createMultipartUpload(MultipartUploadDTO inputDto) {
        CreateMultipartUploadResponse response = s3Client.createMultipartUpload(CreateMultipartUploadRequest.builder()
                .bucket(inputDto.getBucketName())
                .key(inputDto.getKey())
                .metadata(inputDto.getMetadata())
                .contentType(inputDto.getContentType())
                .build());

        MultipartUploadDTO dto = new MultipartUploadDTO();
        dto.setUploadId(response.uploadId());
        dto.setKey(response.key());
        return ValueResponse.of(dto);
    }

    @Override
    public ValueResponse<String> completeMultipartUpload(MultipartUploadDTO inputDto) {
        List<CompletedPart> completedParts = multipartUploadManager.toCompletedPartList(inputDto.getParts());

        CompleteMultipartUploadRequest request = CompleteMultipartUploadRequest.builder()
                .bucket(inputDto.getBucketName())
                .uploadId(inputDto.getUploadId())
                .key(inputDto.getKey())
                .multipartUpload(CompletedMultipartUpload.builder()
                        .parts(completedParts)
                        .build())
                .build();
        CompleteMultipartUploadResponse response = s3Client.completeMultipartUpload(request);
        return ValueResponse.of(response.location());
    }
}
