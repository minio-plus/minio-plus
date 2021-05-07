package org.quantum.minio.plus.dto;

import com.amazonaws.services.s3.model.PartETag;

import java.util.List;

/**
 * @author ike
 * @date 2021 年 05 月 07 日 11:27
 */
public class ComposeUploadPartDTO {

    private String bucketName;

    private String key;

    private String uploadId;

    private List<UploadPartDTO> parts;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public List<UploadPartDTO> getParts() {
        return parts;
    }

    public void setParts(List<UploadPartDTO> parts) {
        this.parts = parts;
    }
}
