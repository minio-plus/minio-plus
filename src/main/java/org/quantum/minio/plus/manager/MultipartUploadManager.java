package org.quantum.minio.plus.manager;

import org.quantum.minio.plus.dto.UploadPartDTO;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.CompletedPart;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ike
 * @date 2021 年 05 月 14 日 9:37
 */
@Component
public class MultipartUploadManager {

    /**
     * 转 CompletedPartList
     * @param dtos
     * @return
     */
    public List<CompletedPart> toCompletedPartList(List<UploadPartDTO> dtos){
        List<CompletedPart> completedParts = new ArrayList<>();
        dtos.forEach(part -> {
            completedParts.add(CompletedPart.builder()
                    .eTag(part.getETag())
                    .partNumber(part.getPartNumber())
                    .build());
        });
        return completedParts;
    }
}
