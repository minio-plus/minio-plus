package org.quantum.minio.plus.service;

import org.quantum.minio.plus.dto.MultipartUploadDTO;
import org.quantum.minio.plus.dto.UploadPartDTO;
import software.amazon.awssdk.services.s3.model.CompletedPart;

import java.util.List;

/**
 * @author jpx10
 */
public interface MultipartUploadService {

    /**
     * 获取多部分上传列表
     * @param bucketName 桶
     * @return
     */
    List<MultipartUploadDTO> getMultipartUploadList(String bucketName);

    /**
     * 创建多部分上传
     * @param inputDto 输入传输对象
     * @return
     */
    MultipartUploadDTO createMultipartUpload(MultipartUploadDTO inputDto);

    /**
     * 完成多部分上传
     * @param inputDto
     * @return
     */
    String completeMultipartUpload(MultipartUploadDTO inputDto);

    /**
     * 转 CompletedPartList
     * @param dtos
     * @return
     */
    List<CompletedPart> toCompletedPartList(List<UploadPartDTO> dtos);
}
