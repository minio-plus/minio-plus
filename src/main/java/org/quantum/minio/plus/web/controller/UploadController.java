package org.quantum.minio.plus.web.controller;

import org.quantum.minio.plus.dto.ComposeUploadPartDTO;
import org.quantum.minio.plus.dto.UploadPartDTO;
import org.quantum.minio.plus.dto.query.PartQuery;
import org.quantum.minio.plus.service.UploadService;
import org.quantum.nucleus.component.dto.MultiResponse;
import org.quantum.nucleus.component.dto.SingleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ike
 * @date 2021 年 05 月 09 日 17:49
 */
@RequestMapping("/upload")
@RestController
public class UploadController {

    private UploadService uploadService;

    @Autowired
    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    /**
     * 获取上传部分
     * @param query 查询
     * @return
     */
    @GetMapping("/part/list")
    public MultiResponse<UploadPartDTO> getUploadPartList(PartQuery query) {
        List<UploadPartDTO> dtos = uploadService.getPartList(query);
        return MultiResponse.of(dtos);
    }

    /**
     * 上传部分合成
     * @param dto
     * @return
     */
    @PostMapping("/part/compose")
    public SingleResponse<String> composeUploadPart(@RequestBody ComposeUploadPartDTO dto){
        String location = uploadService.composePart(dto);
        return SingleResponse.of(location);
    }
}
