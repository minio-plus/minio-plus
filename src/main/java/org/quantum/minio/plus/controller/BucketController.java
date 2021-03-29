package org.quantum.minio.plus.controller;

import org.quantum.minio.plus.dto.BucketDTO;
import org.quantum.minio.plus.dto.LifecycleCreateDTO;
import org.quantum.minio.plus.service.BucketServiceI;
import org.quantum.nucleus.component.dto.MultiResponse;
import org.quantum.nucleus.component.dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ike
 * @date 2021 年 03 月 29 日 16:33
 */
@RequestMapping("/bucket")
@RestController
public class BucketController {

    private BucketServiceI bucketService;

    @Autowired
    public BucketController(BucketServiceI bucketService) {
        this.bucketService = bucketService;
    }

    @GetMapping("/list")
    public MultiResponse<BucketDTO> getList() {
        List<BucketDTO> dtos = bucketService.getList();
        return MultiResponse.of(dtos);
    }

    @PostMapping
    public Response create(@RequestBody BucketDTO dto){
        bucketService.create(dto);
        return Response.buildSuccess();
    }

    @DeleteMapping
    public Response delete(@RequestParam String name){
        bucketService.remove(name);
        return Response.buildSuccess();
    }

    @GetMapping("/lifecycle/list")
    public MultiResponse<String> getLifecycleList(){
        return MultiResponse.buildSuccess();
    }

    @PostMapping("/lifecycle")
    public Response creareLifecycle(LifecycleCreateDTO dto){
        return Response.buildSuccess();
    }

    @DeleteMapping("/lifecycle")
    public Response deleteLifecycle(){

        return Response.buildSuccess();
    }
}
