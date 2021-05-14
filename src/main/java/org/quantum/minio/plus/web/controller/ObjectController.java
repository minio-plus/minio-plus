package org.quantum.minio.plus.web.controller;

import org.quantum.minio.plus.ListResponse;
import org.quantum.minio.plus.Response;
import org.quantum.minio.plus.dto.ObjectDTO;
import org.quantum.minio.plus.dto.query.ObjectQuery;
import org.quantum.minio.plus.service.ObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public ListResponse<ObjectDTO> getList(@RequestBody ObjectQuery query) {
        return objectService.getList(query);
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
        return Response.ok();
    }

    /**
     * 删除文件
     * @param bucketName
     * @param objectName
     * @return
     */
    @DeleteMapping
    public Response delete(@RequestParam String bucketName, @RequestParam String objectName) {
        return objectService.delete(bucketName, objectName);
    }
}
