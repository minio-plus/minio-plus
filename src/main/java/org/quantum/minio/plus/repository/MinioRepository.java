package org.quantum.minio.plus.repository;

import io.minio.ListObjectsArgs;
import io.minio.Result;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import io.minio.messages.Tags;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
     * 获取桶标签
     * @param name
     * @return
     */
    Optional<Tags> getBucketTags(String name);

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
     * @param args 列表对象参数
     * @return
     */
    Iterable<Result<Item>> getObjects(ListObjectsArgs args);
}
