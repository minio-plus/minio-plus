package org.quantum.minio.plus.web.controller;

import org.quantum.minio.plus.ListResponse;
import org.quantum.minio.plus.Response;
import org.quantum.minio.plus.ValueResponse;
import org.quantum.minio.plus.dto.BucketDTO;
import org.quantum.minio.plus.dto.BucketLifecycleRuleDTO;
import org.quantum.minio.plus.dto.query.BucketLifecycleConfigurationQuery;
import org.quantum.minio.plus.service.BucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public ListResponse<BucketDTO> getList() {
        return bucketService.getList();
    }

    @PostMapping
    public Response create(@Valid @RequestBody BucketDTO dto) {
        return bucketService.create(dto);
    }

    @DeleteMapping
    public Response delete(@RequestParam String name) {
        return bucketService.remove(name);
    }

    @GetMapping("/policy")
    public ValueResponse<String> getPolicy(@RequestParam String bucket){
        return bucketService.getPolicy(bucket);
    }

    @GetMapping("/lifecycle/rule/list")
    public ListResponse<BucketLifecycleRuleDTO> getLifecycleRuleList(@Valid BucketLifecycleConfigurationQuery query) {
        return bucketService.getLifecycleRuleList(query);
    }

    @PostMapping("/lifecycle/rule")
    public Response creareLifecycleRule(@RequestBody BucketLifecycleRuleDTO dto) {
        return bucketService.createLifecycleRule(dto);
    }

    @PutMapping("/lifecycle/rule")
    public Response updateLifecycleRule(@RequestBody BucketLifecycleRuleDTO dto) {
        return Response.ok();
    }

    @DeleteMapping("/lifecycle/rule")
    public Response deleteLifecycleRule(@RequestParam("bucketName") String bucketName, @RequestParam("id") String id) {
        return bucketService.deleteLifecycleRule(bucketName, id);
    }
}
