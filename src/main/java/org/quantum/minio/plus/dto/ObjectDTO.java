package org.quantum.minio.plus.dto;

import java.time.ZonedDateTime;

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
    private ZonedDateTime lastModified;

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

    public ZonedDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(ZonedDateTime lastModified) {
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
}
