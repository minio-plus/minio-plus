package org.quantum.minio.plus.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author ike
 * @date 2021 年 05 月 07 日 11:12
 */
public class UploadPartDTO {

    @JsonProperty("eTag")
    private String eTag;

    private Integer partNumber;

    private Long Size;

    private String bucketName;

    private String key;

    private String uploadId;

    public String getETag() {
        return eTag;
    }

    public void setETag(String eTag) {
        this.eTag = eTag;
    }

    public Integer getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(Integer partNumber) {
        this.partNumber = partNumber;
    }

    public Long getSize() {
        return Size;
    }

    public void setSize(Long size) {
        Size = size;
    }

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
}
