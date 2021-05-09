package org.quantum.minio.plus.service.impl;

import org.quantum.minio.plus.dto.ComposeUploadPartDTO;
import org.quantum.minio.plus.dto.UploadPartDTO;
import org.quantum.minio.plus.dto.query.PartQuery;
import org.quantum.minio.plus.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ike
 * @date 2021 年 05 月 09 日 17:49
 */
@Service
public class UploadServiceImpl implements UploadService {

    private S3Client s3Client;

    @Autowired
    public void setMinioClient(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public List<UploadPartDTO> getPartList(PartQuery partQuery) {
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
    public String composePart(ComposeUploadPartDTO dto) {
        List<CompletedPart> completedParts = new ArrayList<>();
        dto.getParts().forEach(part -> {
            completedParts.add(CompletedPart.builder()
                    .eTag(part.getETag())
                    .partNumber(part.getPartNumber())
                    .build());
        });

        CompleteMultipartUploadResponse response = s3Client.completeMultipartUpload(CompleteMultipartUploadRequest.builder()
                .bucket(dto.getBucketName())
                .key(dto.getKey())
                .uploadId(dto.getUploadId())
                .multipartUpload(CompletedMultipartUpload.builder()
                        .parts(completedParts)
                        .build())
                .build());
        return response.location();
    }
}
