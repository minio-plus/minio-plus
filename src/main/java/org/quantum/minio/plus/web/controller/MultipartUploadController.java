package org.quantum.minio.plus.web.controller;

import org.quantum.minio.plus.dto.MultipartUploadDTO;
import org.quantum.minio.plus.service.MultipartUploadService;
import org.quantum.nucleus.component.dto.MultiResponse;
import org.quantum.nucleus.component.dto.SingleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
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
    public MultiResponse<MultipartUploadDTO> getMultipartUploadList(@RequestParam String bucketName){
        List<MultipartUploadDTO> dtos = multipartUploadService.getMultipartUploadList(bucketName);
        return MultiResponse.of(dtos);
    }

    /**
     * 初始化多部分上传
     * @param inputDto 输入传输对象
     * @return
     */
    @PostMapping("/create")
    public SingleResponse<MultipartUploadDTO> initiateMultipartUpload(@RequestBody MultipartUploadDTO inputDto){
        MultipartUploadDTO outDto = multipartUploadService.createMultipartUpload(inputDto);
        return SingleResponse.of(outDto);
    }

    /**
     * 完成多部分上传
     * @param inputDto
     * @return
     */
    @PostMapping("/complete")
    public SingleResponse<String> completeMultipartUpload(@RequestBody MultipartUploadDTO inputDto){
        String location = multipartUploadService.completeMultipartUpload(inputDto);
        return SingleResponse.of(location);
    }
}
