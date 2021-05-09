package org.quantum.minio.plus.dto;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * 多部分上传 传输对象
 * @author ike
 * @date 2021 年 05 月 07 日 11:07
 */
public class MultipartUploadDTO {

    /**
     * 桶名称
     */
    private String bucketName;

    /**
     * 上传标识
     */
    private String uploadId;

    /**
     * 键
     */
    private String key;

    /**
     * 存储类
     */
    private String storageClass;

    /**
     * 元数据
     */
    private Map<String, String> metadata;

    /**
     * 内容类型
     */
    private String contentType;

    /**
     * 开始时间
     */
    private Instant initiated;

    /**
     * 部分
     */
    private List<UploadPartDTO> parts;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStorageClass() {
        return storageClass;
    }

    public void setStorageClass(String storageClass) {
        this.storageClass = storageClass;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Instant getInitiated() {
        return initiated;
    }

    public void setInitiated(Instant initiated) {
        this.initiated = initiated;
    }

    public List<UploadPartDTO> getParts() {
        return parts;
    }

    public void setParts(List<UploadPartDTO> parts) {
        this.parts = parts;
    }
}
