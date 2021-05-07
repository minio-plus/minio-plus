package org.quantum.minio.plus.dto;

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
}
