package org.quantum.minio.plus.web.controller;

import org.quantum.minio.plus.dto.ComposeUploadPartDTO;
import org.quantum.minio.plus.dto.MultipartUploadDTO;
import org.quantum.minio.plus.dto.ObjectDTO;
import org.quantum.minio.plus.dto.UploadPartDTO;
import org.quantum.minio.plus.dto.query.ObjectQuery;
import org.quantum.minio.plus.dto.query.PartQuery;
import org.quantum.minio.plus.service.ObjectService;
import org.quantum.nucleus.component.dto.MultiResponse;
import org.quantum.nucleus.component.dto.Response;
import org.quantum.nucleus.component.dto.SingleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author ike
 * @date 2021 年 03 月 29 日 16:33
 */
@RequestMapping("/object")
@RestController
public class ObjectController {

    private ObjectService objectService;

    @Autowired
    public ObjectController(ObjectService objectService) {
        this.objectService = objectService;
    }

    /**
     * 对象列表
     * @param query
     * @return
     */
    @PostMapping("/list")
    public MultiResponse<ObjectDTO> getList(@RequestBody ObjectQuery query) {
        List<ObjectDTO> dtos = objectService.getList(query);
        return MultiResponse.of(dtos);
    }

    /**
     * 多部分上传列表
     * @param bucketName
     * @return
     */
    @GetMapping("/multipart-upload/list")
    public MultiResponse<MultipartUploadDTO> getMultipartUploadList(@RequestParam String bucketName){
        List<MultipartUploadDTO> dtos = objectService.getMultipartUploadList(bucketName);
        return MultiResponse.of(dtos);
    }

    /**
     * 获取上传部分
     * @param query 查询
     * @return
     */
    @GetMapping("/upload/part/list")
    public MultiResponse<UploadPartDTO> getUploadPartList(PartQuery query) {
        List<UploadPartDTO> dtos = objectService.getUploadPartList(query);
        return MultiResponse.of(dtos);
    }

    /**
     * 初始化多部分上传
     * @param inputDto 输入传输对象
     * @return
     */
    @PostMapping("/upload/multipart/initiate")
    public SingleResponse<MultipartUploadDTO> initiateMultipartUpload(@RequestBody MultipartUploadDTO inputDto){
        MultipartUploadDTO outDto = objectService.initiateMultipartUpload(inputDto);
        return SingleResponse.of(outDto);
    }

    /**
     * 上传部分合成
     * @param dto
     * @return
     */
    @PostMapping("/upload/part/compose")
    public SingleResponse<String> composeUploadPart(@RequestBody ComposeUploadPartDTO dto){
        String location = objectService.composeUploadPart(dto);
        return SingleResponse.of(location);
    }

    /**
     * 获取预签名上传部分URL
     * @param dto
     * @return
     */
    @GetMapping("/presigned/url/uploadpart")
    public SingleResponse<String> getPresignUploadPartUrl(UploadPartDTO dto) {
        String url = objectService.getPresignUploadPartUrl(dto);
        return SingleResponse.of(url);
    }

    /**
     * 获取签名Url
     * @param bucketName
     * @param objectName
     * @return
     */
    @GetMapping("/presigned/url")
    public SingleResponse<String> getPresignedUrl(
            @RequestParam String bucketName,
            @RequestParam String objectName) {
        String url = objectService.getPresignedUrl(bucketName, objectName);
        return SingleResponse.of(url);
    }

    /**
     * 获取签名Url
     * @param dto
     * @return
     */
    @PutMapping("/presigned/url")
    public SingleResponse<String> getPresignedUrl(@RequestBody ObjectDTO dto) {
        String url = objectService.getPresignedUrl(dto);
        return SingleResponse.of(url);
    }

    /**
     * 创建文件夹
     * @param dto
     * @return
     */
    @PostMapping("/folder")
    public Response createFolder(@RequestBody ObjectDTO dto) {
        dto.setObjectName(String.format("%s/", dto.getObjectName()));
        objectService.create(dto);
        return Response.buildSuccess();
    }

    /**
     * 删除文件
     * @param bucketName
     * @param objectName
     * @return
     */
    @DeleteMapping
    public Response delete(@RequestParam String bucketName, @RequestParam String objectName) {
        objectService.delete(bucketName, objectName);
        return Response.buildSuccess();
    }
}
