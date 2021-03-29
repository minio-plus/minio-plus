package org.quantum.minio.plus.service;

import org.quantum.minio.plus.dto.BucketDTO;

import java.util.List;

/**
 * @author ike
 * @date 2021 年 03 月 29 日 17:31
 */
public interface BucketServiceI {

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
}
