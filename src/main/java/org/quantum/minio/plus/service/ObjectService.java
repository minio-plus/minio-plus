package org.quantum.minio.plus.service;

import org.quantum.minio.plus.dto.ObjectDTO;
import org.quantum.minio.plus.dto.query.ObjectQuery;

import java.io.InputStream;
import java.util.List;

/**
 * @author ike
 * @date 2021 年 03 月 29 日 17:52
 */
public interface ObjectService {

    /**
     * 获取列表
     * @param query 查询
     * @return
     */
    List<ObjectDTO> getList(ObjectQuery query);

    /**
     * 创建
     * @param dto 传输对象
     * @param inputStream 输入流
     * @return
     */
    void create(ObjectDTO dto, InputStream inputStream);

    /**
     * 创建
     * @param dto 传输对象
     * @return
     */
    void create(ObjectDTO dto);
}
