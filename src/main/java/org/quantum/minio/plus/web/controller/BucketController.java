package org.quantum.minio.plus.web.controller;

import org.quantum.minio.plus.dto.BucketDTO;
import org.quantum.minio.plus.dto.BucketLifecycleRuleDTO;
import org.quantum.minio.plus.dto.LifecycleCreateDTO;
import org.quantum.minio.plus.dto.query.BucketLifecycleConfigurationQuery;
import org.quantum.minio.plus.service.BucketService;
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

    private BucketService bucketService;

    @Autowired
    public BucketController(BucketService bucketService) {
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

    @GetMapping("/lifecycle/rule/list")
    public MultiResponse<BucketLifecycleRuleDTO> getLifecycleRuleList(BucketLifecycleConfigurationQuery query) {
        List<BucketLifecycleRuleDTO> lifecycleRuleDtos = bucketService.getLifecycleRuleList(query);
        return MultiResponse.of(lifecycleRuleDtos);
    }

    @PostMapping("/lifecycle/rule")
    public Response creareLifecycleRule(@RequestBody BucketLifecycleRuleDTO dto) {
        bucketService.createLifecycleRule(dto);
        return Response.buildSuccess();
    }

    @DeleteMapping("/lifecycle/rule")
    public Response deleteLifecycleRule(@RequestParam("bucketName") String bucketName, @RequestParam("id") String id) {
        bucketService.deleteLifecycleRule(bucketName, id);
        return Response.buildSuccess();
    }
}
