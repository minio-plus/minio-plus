package org.quantum.minio.plus.repository;

import io.minio.ListObjectsArgs;
import io.minio.Result;
import io.minio.messages.Bucket;
import io.minio.messages.Item;

import java.util.List;

/**
 * @author jpx10
 */
public interface MinioRepository {

    /**
     * 获取桶列表
     * @return
     */
    List<Bucket> getBuckets();

    /**
     * 创建桶
     * @param name 名称
     */
    void createBucket(String name);

    /**
     * 删除桶
     * @param name 名称
     */
    void deleteBucket(String name);

    /**
     * 获取对象列表
     * @return
     */
    Iterable<Result<Item>> getObjects();
}
