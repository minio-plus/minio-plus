package org.quantum.minio.plus.web.controller;

import org.quantum.minio.plus.ListResponse;
import org.quantum.minio.plus.ValueResponse;
import org.quantum.minio.plus.dto.MultipartUploadDTO;
import org.quantum.minio.plus.service.MultipartUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 多部分上传控制器
 * @author ike
 * @date 2021 年 05 月 09 日 17:42
 */
@RequestMapping("/multipart/upload")
@RestController
public class MultipartUploadController {

    private MultipartUploadService multipartUploadService;

    @Autowired
    public MultipartUploadController(MultipartUploadService multipartUploadService) {
        this.multipartUploadService = multipartUploadService;
    }

    /**
     * 多部分上传列表
     * @param bucketName
     * @return
     */
    @GetMapping("/list")
    public ListResponse<MultipartUploadDTO> getMultipartUploadList(@RequestParam String bucketName){
        return multipartUploadService.getMultipartUploadList(bucketName);
    }

    /**
     * 初始化多部分上传
     * @param inputDto 输入传输对象
     * @return
     */
    @PostMapping("/create")
    public ValueResponse<MultipartUploadDTO> createMultipartUpload(@RequestBody MultipartUploadDTO inputDto) {
        return multipartUploadService.createMultipartUpload(inputDto);
    }

    /**
     * 完成多部分上传
     * @param inputDto
     * @return
     */
    @PostMapping("/complete")
    public ValueResponse<String> completeMultipartUpload(@RequestBody MultipartUploadDTO inputDto){
        return multipartUploadService.completeMultipartUpload(inputDto);
    }
}
