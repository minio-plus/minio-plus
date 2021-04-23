package org.quantum.minio.plus.dto.query;

import java.util.List;

/**
 * 对象查询
 * @author ike
 * @date 2021 年 03 月 30 日 17:37
 */
public class ObjectQuery {

    private String bucketName;

    private List<String> prefixs;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public List<String> getPrefixs() {
        return prefixs;
    }

    public void setPrefixs(List<String> prefixs) {
        this.prefixs = prefixs;
    }
}
