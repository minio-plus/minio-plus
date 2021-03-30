package org.quantum.minio.plus.dto;

import java.time.ZonedDateTime;
import java.util.Map;

/**
 * 桶 数据传输对象
 * @author ike
 * @date 2021 年 03 月 29 日 17:29
 */
public class BucketDTO {

    private String name;

    private Map<String, String> tags;

    private ZonedDateTime creationDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
