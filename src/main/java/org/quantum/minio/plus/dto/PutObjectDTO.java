package org.quantum.minio.plus.dto;

import java.util.Map;

/**
 * put 对象数据传输对象
 * @author ike
 * @date 2021 年 05 月 09 日 18:06
 */
public class PutObjectDTO {

    private String bucketName;

    private String objectName;

    private String contentType;

    private Map<String, String> metadata;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
