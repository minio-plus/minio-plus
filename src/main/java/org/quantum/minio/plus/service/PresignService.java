package org.quantum.minio.plus.service;

import org.quantum.minio.plus.ValueResponse;
import org.quantum.minio.plus.dto.MultipartUploadDTO;
import org.quantum.minio.plus.dto.PutObjectDTO;
import org.quantum.minio.plus.dto.UploadPartDTO;
import org.quantum.minio.plus.dto.query.ObjectQuery;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 签名服务
 * @author ike
 * @date 2021 年 05 月 09 日 17:09
 */
public interface PresignService {

    /**
     * 获取对象
     * @param query 查询
     * @return
     */
    ValueResponse<String> getObject(ObjectQuery query);

    /**
     * put 对象
     * @param dto 传输对象
     * @return
     */
    ValueResponse<String> putObject(PutObjectDTO dto);

    /**
     * 上传部分
     * @param dto 传输对象
     * @return
     */
    ValueResponse<String> uploadPart(UploadPartDTO dto);

    /**
     * 创建多部分上传
     * @param dto
     * @return
     */
    ValueResponse<String> createMultipartUpload(MultipartUploadDTO dto);

    /**
     * 终止多部分上传
     * @param dto 传输对象
     * @return
     */
    ValueResponse<String> abortMultipartUpload(MultipartUploadDTO dto);

    /**
     * 完成多部分上传
     * @param dto 传输对象
     * @return
     */
    ValueResponse<String> completeMultipartUpload(MultipartUploadDTO dto);
}
