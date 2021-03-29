package org.quantum.minio.plus.dto;

import java.time.ZonedDateTime;

/**
 * @author ike
 * @date 2021 年 03 月 29 日 17:53
 */
public class ObjectDTO {

    private String etag;

    /**
     * 名称
     */
    private String name;

    /**
     * 大小
     */
    private Long size;

    /**
     * 类型
     */
    private String type;

    /**
     * 最后更新时间
     */
    private ZonedDateTime lastUpdateTime;

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ZonedDateTime getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(ZonedDateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
