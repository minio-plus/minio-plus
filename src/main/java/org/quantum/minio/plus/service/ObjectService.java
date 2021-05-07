package org.quantum.minio.plus.service;

import org.quantum.minio.plus.dto.ComposeUploadPartDTO;
import org.quantum.minio.plus.dto.MultipartUploadDTO;
import org.quantum.minio.plus.dto.ObjectDTO;
import org.quantum.minio.plus.dto.UploadPartDTO;
import org.quantum.minio.plus.dto.query.ObjectQuery;
import org.quantum.minio.plus.dto.query.PartQuery;

import java.util.List;
import java.util.Map;

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
     * 初始化多部分上传
     * @param inputDto 输入传输对象
     * @return
     */
    MultipartUploadDTO initiateMultipartUpload(MultipartUploadDTO inputDto);

    /**
     * 获取多部分上传列表
     * @param bucketName
     * @return
     */
    List<MultipartUploadDTO> getMultipartUploadList(String bucketName);

    /**
     * 获取上传部分列表
     * @param partQuery 部分查询
     * @return
     */
    List<UploadPartDTO> getUploadPartList(PartQuery partQuery);

    /**
     * 合成上传部分
     * @param dto 传输对象
     * @return
     */
    String composeUploadPart(ComposeUploadPartDTO dto);

    /**
     * 获取预签名
     * @param bucketName 桶名称
     * @param objectName 对象名称
     * @param method 方式
     * @return
     */
    String getPresignedUrl(String bucketName, String objectName, String method);

    /**
     * 获取预签名表单数据
     * @param bucketName
     * @param objectName
     * @return
     */
    Map<String, String> getPresignedFormData(String bucketName, String objectName);

    /**
     * 创建
     * @param dto 传输对象
     * @return
     */
    void create(ObjectDTO dto);

    /**
     * 合并
     * @param bucketName
     * @param objectName
     * @param parts
     * @return
     */
    String compose(String bucketName, String objectName, List<String> parts);


    /**
     * 删除
     * @param objectName
     */
    void delete(String bucketName, String objectName);
}
