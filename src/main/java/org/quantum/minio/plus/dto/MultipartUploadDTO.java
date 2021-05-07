package org.quantum.minio.plus.dto;

/**
 * @author ike
 * @date 2021 年 05 月 07 日 11:07
 */
public class MultipartUploadDTO {

    private String uploadId;

    private String key;

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
}
