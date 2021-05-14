package org.quantum.minio.plus.service;

import org.quantum.minio.plus.ListResponse;
import org.quantum.minio.plus.ValueResponse;
import org.quantum.minio.plus.Response;
import org.quantum.minio.plus.dto.BucketDTO;
import org.quantum.minio.plus.dto.BucketLifecycleRuleDTO;
import org.quantum.minio.plus.dto.query.BucketLifecycleConfigurationQuery;

/**
 * @author ike
 * @date 2021 年 03 月 29 日 17:31
 */
public interface BucketService {

    /**
     * 获取列表
     * @return 列表响应
     */
    ListResponse<BucketDTO> getList();

    /**
     * 获取策略
     * @param bucket
     * @return 值响应
     */
    ValueResponse<String> getPolicy(String bucket);

    /**
     * 创建
     * @param dto
     * @return 响应
     */
    Response create(BucketDTO dto);

    /**
     * 移除
     * @param name
     * @return 响应
     */
    Response remove(String name);

    /**
     * 获取生命周期规则列表
     * @param lifecycleConfigurationQuery 生命周期配置查询
     * @return
     */
    ListResponse<BucketLifecycleRuleDTO> getLifecycleRuleList(BucketLifecycleConfigurationQuery lifecycleConfigurationQuery);

    /**
     * 创建生命周期规则
     * @param lifecycleRuleDto 生命周期规则数据传输对象
     * @return 响应
     */
    Response createLifecycleRule(BucketLifecycleRuleDTO lifecycleRuleDto);

    /**
     * 删除生命周期规则
     * @param bucketName 桶名称
     * @param id 标识
     * @return 响应
     */
    Response deleteLifecycleRule(String bucketName, String id);
}
