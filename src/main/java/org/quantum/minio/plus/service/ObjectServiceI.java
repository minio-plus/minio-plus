package org.quantum.minio.plus.service;

import org.quantum.minio.plus.dto.ObjectDTO;

import java.util.List;

/**
 * @author ike
 * @date 2021 年 03 月 29 日 17:52
 */
public interface ObjectServiceI {

    /**
     * 获取列表
     * @return
     */
    List<ObjectDTO> getList();
}
