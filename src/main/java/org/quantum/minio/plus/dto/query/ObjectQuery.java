package org.quantum.minio.plus.dto.query;

/**
 * 对象查询
 * @author ike
 * @date 2021 年 03 月 30 日 17:37
 */
public class ObjectQuery {

    private String bucketName;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }
}
