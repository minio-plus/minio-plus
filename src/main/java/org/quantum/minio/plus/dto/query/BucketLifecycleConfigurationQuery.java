package org.quantum.minio.plus.dto.query;

/**
 * @author ike
 * @date 2021 年 04 月 08 日 17:23
 */
public class BucketLifecycleConfigurationQuery {

    private String bucketName;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }
}
