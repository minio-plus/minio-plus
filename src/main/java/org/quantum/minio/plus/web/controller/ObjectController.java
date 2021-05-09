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
