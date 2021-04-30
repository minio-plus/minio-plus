package org.quantum.minio.plus.service;

import org.quantum.minio.plus.dto.BucketDTO;
import org.quantum.minio.plus.dto.BucketLifecycleRuleDTO;
import org.quantum.minio.plus.dto.query.BucketLifecycleConfigurationQuery;

import java.util.List;

/**
 * @author ike
 * @date 2021 年 03 月 29 日 17:31
 */
public interface BucketService {

    /**
     * 获取列表
     * @return
     */
    List<BucketDTO> getList();

    /**
     * 创建
     * @param dto
     */
    void create(BucketDTO dto);

    /**
     * 移除
     * @param name
     */
    void remove(String name);

    /**
     * 获取生命周期规则列表
     * @param lifecycleConfigurationQuery 生命周期配置查询
     * @return
     */
    List<BucketLifecycleRuleDTO> getLifecycleRuleList(BucketLifecycleConfigurationQuery lifecycleConfigurationQuery);

    /**
     * 创建生命周期规则
     * @param lifecycleRuleDto 生命周期规则数据传输对象
     */
    void createLifecycleRule(BucketLifecycleRuleDTO lifecycleRuleDto);

    /**
     * 删除生命周期规则
     * @param bucketName 桶名称
     * @param id 标识
     */
    void deleteLifecycleRule(String bucketName, String id);
}
