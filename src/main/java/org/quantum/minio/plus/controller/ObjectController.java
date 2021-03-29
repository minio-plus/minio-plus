package org.quantum.minio.plus.controller;

import org.quantum.minio.plus.dto.ObjectDTO;
import org.quantum.minio.plus.service.ObjectServiceI;
import org.quantum.nucleus.component.dto.MultiResponse;
import org.quantum.nucleus.component.dto.Response;
import org.quantum.nucleus.component.dto.SingleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author ike
 * @date 2021 年 03 月 29 日 16:33
 */
@RequestMapping("/object")
@RestController
public class ObjectController {

    private ObjectServiceI objectService;

    @Autowired
    public ObjectController(ObjectServiceI objectService) {
        this.objectService = objectService;
    }

    @GetMapping("/list")
    public MultiResponse<ObjectDTO> getList() {
        List<ObjectDTO> dtos = objectService.getList();
        return MultiResponse.of(dtos);
    }

    @RequestMapping("/upload")
    public SingleResponse upload(MultipartFile multipartFile) {
        return SingleResponse.buildSuccess();
    }

    @DeleteMapping
    public Response delete(@RequestParam String name){
        return Response.buildSuccess();
    }
}
