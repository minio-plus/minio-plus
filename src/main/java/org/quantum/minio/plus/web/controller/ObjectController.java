package org.quantum.minio.plus.web.controller;

import org.quantum.minio.plus.dto.ObjectDTO;
import org.quantum.minio.plus.dto.query.ObjectQuery;
import org.quantum.minio.plus.service.ObjectService;
import org.quantum.nucleus.component.dto.MultiResponse;
import org.quantum.nucleus.component.dto.Response;
import org.quantum.nucleus.component.dto.SingleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @GetMapping("/list")
    public MultiResponse<ObjectDTO> getList(ObjectQuery query) {
        List<ObjectDTO> dtos = objectService.getList(query);
        return MultiResponse.of(dtos);
    }

    @RequestMapping("/upload")
    public SingleResponse upload(@RequestParam String bucketName, @RequestPart("file") MultipartFile multipartFile) {
        try {
            ObjectDTO dto = new ObjectDTO();
            dto.setBucketName(bucketName);
            dto.setObjectName(multipartFile.getOriginalFilename());
            dto.setSize(multipartFile.getSize());
            dto.setContentType(multipartFile.getContentType());
            objectService.create(dto, multipartFile.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SingleResponse.buildSuccess();
    }

    @PostMapping("/folder")
    public Response createFolder(@RequestBody ObjectDTO dto) {
        dto.setObjectName(String.format("%s/", dto.getObjectName()));
        objectService.create(dto);
        return Response.buildSuccess();
    }

    @DeleteMapping
    public Response delete(@RequestParam String name) {
        return Response.buildSuccess();
    }
}
