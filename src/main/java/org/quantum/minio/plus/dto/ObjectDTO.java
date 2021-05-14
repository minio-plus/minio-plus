package org.quantum.minio.plus.dto;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * @author ike
 * @date 2021 年 03 月 29 日 17:53
 */
public class ObjectDTO {

    /**
     * 标识
     */
    private String etag;

    /**
     * 对象名称
     */
    private String objectName;

    /**
     * 最后更新时间
     */
    private Instant lastModified;

    /**
     * 存储类
     */
    private String storageClass;

    /**
     * 大小
     */
    private Long size;


    /**
     * 桶名称
     */
    private String bucketName;

    /**
     * 内容类型
     */
    private String contentType;

    /**
     * 是否是目录
     */
    private Boolean isDir;

    /**
     * 用户元数据
     */
    private Map<String, String> metadata;

    /**
     * 前缀列表
     */
    private List<String> prefixs;

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public Instant getLastModified() {
        return lastModified;
    }

    public void setLastModified(Instant lastModified) {
        this.lastModified = lastModified;
    }

    public String getStorageClass() {
        return storageClass;
    }

    public void setStorageClass(String storageClass) {
        this.storageClass = storageClass;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Boolean getDir() {
        return isDir;
    }

    public void setDir(Boolean dir) {
        isDir = dir;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public List<String> getPrefixs() {
        return prefixs;
    }

    public void setPrefixs(List<String> prefixs) {
        this.prefixs = prefixs;
    }
}
