package org.quantum.minio.plus.service;

import org.quantum.minio.plus.ListResponse;
import org.quantum.minio.plus.ValueResponse;
import org.quantum.minio.plus.dto.MultipartUploadDTO;

/**
 * @author jpx10
 */
public interface MultipartUploadService {

    /**
     * 获取多部分上传列表
     * @param bucketName 桶
     * @return
     */
    ListResponse<MultipartUploadDTO> getMultipartUploadList(String bucketName);

    /**
     * 创建多部分上传
     * @param inputDto 输入传输对象
     * @return
     */
    ValueResponse<MultipartUploadDTO> createMultipartUpload(MultipartUploadDTO inputDto);

    /**
     * 完成多部分上传
     * @param inputDto
     * @return
     */
    ValueResponse<String> completeMultipartUpload(MultipartUploadDTO inputDto);
}
