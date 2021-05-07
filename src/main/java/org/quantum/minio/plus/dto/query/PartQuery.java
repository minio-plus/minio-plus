package org.quantum.minio.plus.dto.query;

/**
 *  部分查询
 * @author ike
 * @date 2021 年 05 月 07 日 15:57
 */
public class PartQuery {

    private String bucketName;

    private String key;

    private String uploadId;

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
