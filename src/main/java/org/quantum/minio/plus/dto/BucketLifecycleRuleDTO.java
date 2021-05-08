package org.quantum.minio.plus.dto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Map;

/**
 * 生命周期 创建数据传输对象
 * @author ike
 * @date 2021 年 03 月 29 日 16:39
 */
public class BucketLifecycleRuleDTO {

    /**
     * 标识
     */
    private String id;

    /**
     * 桶
     */
    private String bucketName;

    /**
     * 状态 Disabled-禁用 Enabled-启用
     */
    private String status;

    /**
     * 前缀
     */
    private String prefix;

    /**
     * 标签
     */
    private Map<String, String> tags;

    /**
     * 清除策略
     */
    private String cleanStrategy;

    /**
     * 天数
     */
    private Integer days;

    /**
     * 日期
     */
    private Instant date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public String getCleanStrategy() {
        return cleanStrategy;
    }

    public void setCleanStrategy(String cleanStrategy) {
        this.cleanStrategy = cleanStrategy;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }
}
