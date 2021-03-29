package org.quantum.minio.plus.dto;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 生命周期 创建数据传输对象
 * @author ike
 * @date 2021 年 03 月 29 日 16:39
 */
public class LifecycleCreateDTO {

    /**
     * 桶
     */
    private String bucket;

    /**
     * 状态 0-禁用 1-启用
     */
    private Integer status;

    /**
     * 前缀
     */
    private String prefix;

    /**
     * 标签
     */
    private Map<String, String> tags;

    /**
     * 天数
     */
    private Integer days;

    /**
     * 日期
     */
    private LocalDateTime date;

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
