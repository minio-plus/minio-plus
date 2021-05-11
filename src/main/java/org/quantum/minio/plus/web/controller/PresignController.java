package org.quantum.minio.plus.web.controller;

import org.quantum.minio.plus.dto.MultipartUploadDTO;
import org.quantum.minio.plus.dto.ObjectDTO;
import org.quantum.minio.plus.dto.PutObjectDTO;
import org.quantum.minio.plus.dto.UploadPartDTO;
import org.quantum.minio.plus.dto.query.ObjectQuery;
import org.quantum.minio.plus.service.PresignService;
import org.quantum.nucleus.component.dto.SingleResponse;
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
    public SingleResponse<String> getObject(ObjectQuery query) {
        String url = presignService.getObject(query);
        return SingleResponse.of(url);
    }

    /**
     * 获取签名
     * @param dto
     * @return
     */
    @PutMapping
    public SingleResponse<String> putObject(@RequestBody PutObjectDTO dto) {
        String url = presignService.putObject(dto);
        return SingleResponse.of(url);
    }

    /**
     * 获取完成多部分上传
     * @param dto
     * @return
     */
    @PostMapping("/multipart/upload/complete")
    public SingleResponse<String> getCompleteMultipartUpload(@RequestBody MultipartUploadDTO dto){
        String location = presignService.completeMultipartUpload(dto);
        return SingleResponse.of(location);
    }

    /**
     * 获取终止多部分上传
     * @param dto
     * @return
     */
    @GetMapping("/multipart/upload/abort")
    public SingleResponse<String> getAbortMultipartUpload(MultipartUploadDTO dto) {
        String url = presignService.abortMultipartUpload(dto);
        return SingleResponse.of(url);
    }

    /**
     * 获取上传部分
     * @param dto
     * @return
     */
    @GetMapping("/upload/part")
    public SingleResponse<String> getUploadPart(UploadPartDTO dto) {
        String url = presignService.uploadPart(dto);
        return SingleResponse.of(url);
    }
}
