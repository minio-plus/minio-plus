package org.quantum.minio.plus.web.controller;

import org.quantum.minio.plus.ValueResponse;
import org.quantum.minio.plus.dto.MultipartUploadDTO;
import org.quantum.minio.plus.dto.PutObjectDTO;
import org.quantum.minio.plus.dto.UploadPartDTO;
import org.quantum.minio.plus.dto.query.ObjectQuery;
import org.quantum.minio.plus.service.PresignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 签名控制器
 * @author ike
 * @date 2021 年 05 月 09 日 17:12
 */
@RequestMapping("/presign")
@RestController
public class PresignController {


    private PresignService presignService;

    @Autowired
    public PresignController(PresignService presignService) {
        this.presignService = presignService;
    }


    /**
     * 获取签名
     * @param query 查询
     * @return
     */
    @GetMapping
    public ValueResponse<String> getObject(ObjectQuery query) {
        return presignService.getObject(query);
    }

    /**
     * 获取签名
     * @param dto
     * @return
     */
    @PutMapping
    public ValueResponse<String> putObject(@RequestBody PutObjectDTO dto) {
        return presignService.putObject(dto);
    }

    /**
     * 获取完成多部分上传
     * @param dto
     * @return
     */
    @PostMapping("/multipart/upload/complete")
    public ValueResponse<String> getCompleteMultipartUpload(@RequestBody MultipartUploadDTO dto){
        return presignService.completeMultipartUpload(dto);
    }

    /**
     * 获取终止多部分上传
     * @param dto
     * @return
     */
    @GetMapping("/multipart/upload/abort")
    public ValueResponse<String> getAbortMultipartUpload(MultipartUploadDTO dto) {
        return presignService.abortMultipartUpload(dto);
    }

    /**
     * 获取上传部分
     * @param dto
     * @return
     */
    @GetMapping("/upload/part")
    public ValueResponse<String> getUploadPart(UploadPartDTO dto) {
        return presignService.uploadPart(dto);
    }
}
