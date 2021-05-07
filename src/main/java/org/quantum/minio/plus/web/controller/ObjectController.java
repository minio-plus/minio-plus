package org.quantum.minio.plus.web.controller;

import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.PartListing;
import com.amazonaws.services.s3.model.UploadPartResult;
import org.quantum.minio.plus.dto.ComposeUploadPartDTO;
import org.quantum.minio.plus.dto.MultipartUploadDTO;
import org.quantum.minio.plus.dto.ObjectDTO;
import org.quantum.minio.plus.dto.UploadPartDTO;
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

    @PostMapping("/list")
    public MultiResponse<ObjectDTO> getList(@RequestBody ObjectQuery query) {
        List<ObjectDTO> dtos = objectService.getList(query);
        return MultiResponse.of(dtos);
    }

    @PostMapping("/uploadmultipart")
    public SingleResponse<MultipartUploadDTO> createMultipartUpload(@RequestParam String bucketName, @RequestParam String key){
        MultipartUploadDTO dto = objectService.createMultipartUpload(bucketName, key);
        return SingleResponse.of(dto);
    }

    @GetMapping("/upload/part")
    public MultiResponse<UploadPartDTO> getUploadPart(@RequestParam String bucketName, @RequestParam String key, @RequestParam String uploadId) {
        List<UploadPartDTO> dtos = objectService.getUploadPartList(bucketName, key, uploadId);
        return MultiResponse.of(dtos);
    }

    @PostMapping("/upload/part/compose")
    public SingleResponse<String> composeUploadPart(@RequestBody ComposeUploadPartDTO dto){
        String location = objectService.composeUploadPart(dto);
        return SingleResponse.of(location);
    }



    @RequestMapping("/upload")
    public SingleResponse upload(
            @RequestParam String bucketName,
            @RequestParam Map<String, String> userMetaData,
            @RequestPart("file") MultipartFile multipartFile) {
        try {
            ObjectDTO dto = new ObjectDTO();
            dto.setBucketName(bucketName);
            dto.setObjectName(multipartFile.getOriginalFilename());
            dto.setSize(multipartFile.getSize());
            dto.setContentType(multipartFile.getContentType());
            dto.setUserMetaData(userMetaData);
            objectService.create(dto, multipartFile.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SingleResponse.buildSuccess();
    }

    @GetMapping("/presigned/formdata")
    public SingleResponse<Map<String, String>> getPresignedFormData(
            @RequestParam String bucketName,
            @RequestParam String objectName){
        Map<String, String> map = objectService.getPresignedFormData(bucketName, objectName);
        return SingleResponse.of(map);
    }

    @GetMapping("/presigned/url")
    public SingleResponse<String> getPresignedUrl(
            @RequestParam String bucketName,
            @RequestParam String objectName,
            @RequestParam String method) {
        String url = objectService.getPresignedUrl(bucketName, objectName, method);
        return SingleResponse.of(url);
    }

    @PostMapping("/folder")
    public Response createFolder(@RequestBody ObjectDTO dto) {
        dto.setObjectName(String.format("%s/", dto.getObjectName()));
        objectService.create(dto);
        return Response.buildSuccess();
    }

    @DeleteMapping
    public Response delete(@RequestParam String bucketName, @RequestParam String objectName) {
        objectService.delete(bucketName, objectName);
        return Response.buildSuccess();
    }
}
